package project.domain.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.domain.directory.service.FileService;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.dto.request.FileDownloadDto;
import project.domain.photo.dto.request.FileUploadDto;
import project.domain.photo.dto.request.MultiFileDownloadDto;
import project.domain.photo.entity.Meta;
import project.domain.photo.entity.MetaCode;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.PhotoUrl;
import project.domain.photo.entity.mediatable.DownloadLog;
import project.domain.photo.repository.MetaRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.FileUtil;
import project.global.util.ImageUtil;
import project.global.util.S3Util;
import project.global.util.gptutil.GptUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

import static io.jsonwebtoken.Jwts.header;

@Slf4j
@Service
@RequiredArgsConstructor
public class FIleUploadDownloadServiceImpl implements FileUploadDownloadService{

    private final AmazonS3 amazonS3;
    private final FileService fileService;
    private final PhotoRepository photoRepository;
    private final MemberRepository memberRepository;
    private final MetaRepository metaRepository;
    private final PartyRepository partyRepository;
    // GptUtil
    private final GptUtil gptUtil;
    private final S3Util s3Util;
    private final ImageUtil imageUtil;
    private final FileUtil fileUtil;
    private final WebClient webClient;

    public void uploadProcess(List<MultipartFile> files, FileUploadDto fileUploadDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();
        //S3업로드 커스텀 키 생성
        SSECustomerKey sseKey = new SSECustomerKey(s3Util.generateSSEKey(fileUploadDto.getKey()));

        for(MultipartFile file : files){
            Photo photo = new Photo();
//            photo.setParty(partyRepository.findById(partyId));
            PhotoUrl urls = new PhotoUrl();

            //S3 파일 업로드 후 저장
            urls.setPhotoUrl(s3Util.fileUpload(file, sseKey));
            photo.setFileName(urls.getPhotoUrl().split("/")[3]);

            BufferedImage firstThumbnail = imageUtil.resizeImage(file, 1);
            String firstThumbnailUrl = s3Util.bufferedImageUpload(firstThumbnail, sseKey, file);
            urls.setThumb_url1(firstThumbnailUrl);

            BufferedImage secondThumbnail = imageUtil.resizeImage(file, 1);
            String secondThumbnailUrl = s3Util.bufferedImageUpload(secondThumbnail, sseKey, file);
            urls.setThumb_url2(secondThumbnailUrl);

            log.info("urls : " + urls.getPhotoUrl() + ", " + urls.getThumb_url1() + ", " + urls.getThumb_url2()
            + ", " + photo.getFileName());

            photo.setPhotoUrl(urls);

            Party party = partyRepository.findById(fileUploadDto.getPartyId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
            photo.setParty(party);
            Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
            photo.setMember(member);
            photoRepository.save(photo);


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                ImageIO.write(secondThumbnail, "jpg", outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] imageBytes = outputStream.toByteArray();

            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("file", imageBytes)
                    .filename(urls.getPhotoUrl())
                    .contentType(MediaType.IMAGE_JPEG);
            bodyBuilder.part("partyId", fileUploadDto.getPartyId());
            bodyBuilder.part("key", fileUploadDto.getKey());

            webClient
                    .post()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> {
                        // Handle response from server
                        log.info(response);
                    });

            //MongoDB 업데이트
            fileService.createFile(fileUploadDto.getPartyId(), photo);

            //GPT API
            for (Integer code : gptUtil.postChat(file)) {
                // 받은 메타 코드 저장 Meta 테이블에 저장
                metaRepository.save(
                    Meta.builder()
                        .photo(photo)
                        .metaCode(MetaCode.getEnumByCode(code))
                        .build()
                );
            }
        }
    }

    public S3Object fileDownload(FileDownloadDto fileDownloadDto, String key) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Photo photo = photoRepository.findById(fileDownloadDto.getFileId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        String fileName = photo.getFileName();
        SSECustomerKey sseKey = new SSECustomerKey(s3Util.generateSSEKey(key));

        S3Object object = null;

        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
            object = amazonS3.getObject(getObjectRequest);
        }catch (Exception e){
            throw new BusinessLogicException(ErrorCode.FILE_NOT_FOUND);
        }

        DownloadLog.customBuilder()
                .photo(photo)
                .member(member)
                .build();

        return object;
    }

    public Map<String, List<File>> multiFileDownload(MultiFileDownloadDto multiFileDownloadDto, String key) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<S3Object> objects = new ArrayList<>();
        List<File> files = new ArrayList<>();
        List<Long> fileIds = multiFileDownloadDto.getFileIdList();

        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        SSECustomerKey sseKey = new SSECustomerKey(s3Util.generateSSEKey(key));

        String tempPath = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID();
        File tempDir = new File(tempPath);  // 임시 디렉터리 경로
        tempDir.mkdirs();

        for(long id : fileIds){
            Photo photo = photoRepository.findById(id)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

            String fileName = photo.getFileName();
            S3Object object = null;
            try{
                GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
                object = amazonS3.getObject(getObjectRequest);
            }catch (Exception e){
                throw new BusinessLogicException(ErrorCode.FILE_NOT_FOUND);
            }

            objects.add(object);

            DownloadLog.customBuilder()
                    .photo(photo)
                    .member(member)
                    .build();
        }

        int i = 0;

        for(S3Object object : objects){
            i++;
            InputStream inputStream = object.getObjectContent();
            String type = object.getObjectMetadata().getContentType().split("/")[1];
            File tempFile = new File(tempDir, multiFileDownloadDto.getPrefix() + i + "." + type);
            tempFile.deleteOnExit();
            fileUtil.copyInputStreamToFile(inputStream, tempFile);
            files.add(tempFile);
        }

        HashMap<String, List<File>> returnMap = new HashMap<>();
        returnMap.put(tempPath, files);
        return returnMap;
    }

}
