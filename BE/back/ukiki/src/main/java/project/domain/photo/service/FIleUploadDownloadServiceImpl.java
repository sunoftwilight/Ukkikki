package project.domain.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SSECustomerKey;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.domain.article.entity.PhotoType;
import project.domain.directory.service.FileService;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.repository.MemberRepository;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.dto.request.FileDownloadDto;
import project.domain.photo.dto.request.FileUploadDto;
import project.domain.photo.dto.request.MultiFileDownloadDto;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.PhotoUrl;
import project.domain.photo.entity.mediatable.DownloadLog;
import project.domain.photo.repository.DownloadLogRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.FileUtil;
import project.global.util.ImageUtil;
import project.global.util.S3Util;

@Slf4j
@Service
@RequiredArgsConstructor
public class FIleUploadDownloadServiceImpl implements FileUploadDownloadService{

    private final GptService gptService;
    private final AmazonS3 amazonS3;
    private final FileService fileService;
    private final PhotoRepository photoRepository;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final DownloadLogRepository downloadLogRepository;
    // GptUtil
    private final S3Util s3Util;
    private final ImageUtil imageUtil;
    private final FileUtil fileUtil;
    private final WebClient webClient;

    public List<String> uploadProcess(List<MultipartFile> files, FileUploadDto fileUploadDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();
        log.info("유저 Id = {}", memberId);

        List<String> res = new ArrayList<>();
        String key = fileUploadDto.getKey();

        if (key == null || key.isEmpty()){
            throw new BusinessLogicException(ErrorCode.SSE_KEY_MISSED);
        }

        for(MultipartFile file : files){
            Photo photo = new Photo();
            photo.setPhotoType(PhotoType.APP);
            PhotoUrl urls = new PhotoUrl();

            //S3 파일 업로드 후 저장
            urls.setPhotoUrl(s3Util.fileUpload(file, key));
            photo.setFileName(urls.getPhotoUrl().split("/")[3]);

            BufferedImage firstThumbnail = imageUtil.resizeImage(file, 1);
            String firstThumbnailUrl = s3Util.bufferedImageUpload(firstThumbnail, key, file);
            urls.setThumb_url1(firstThumbnailUrl);

            BufferedImage secondThumbnail = imageUtil.resizeImage(file, 2);
            String secondThumbnailUrl = s3Util.bufferedImageUpload(secondThumbnail, key, file);
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

            //인물분류 API
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                ImageIO.write(firstThumbnail, "jpg", outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] imageBytes = outputStream.toByteArray();

            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("file", imageBytes)
                    .filename(urls.getPhotoUrl())
                    .contentType(MediaType.IMAGE_JPEG);
            bodyBuilder.part("partyId", fileUploadDto.getPartyId());
            bodyBuilder.part("key", key);
            bodyBuilder.part("photoId", photo.getId());

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
            log.info("MongoDB 업데이트 시작");
            //MongoDB 업데이트
            res.add(fileService.createFile(fileUploadDto.getPartyId(), photo));
            //GPT API
            gptService.processGptApiAsync(photo, file);
        }

        return res;
    }

    @Override
    public void uploadToDirectory(List<MultipartFile> files, FileUploadDto fileUploadDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        String key = fileUploadDto.getKey();

        if (key == null || key.isEmpty()){
            throw new BusinessLogicException(ErrorCode.SSE_KEY_MISSED);
        }

        for(MultipartFile file : files){
            Photo photo = new Photo();
            photo.setPhotoType(PhotoType.APP);
            PhotoUrl urls = new PhotoUrl();

            //S3 파일 업로드 후 저장
            urls.setPhotoUrl(s3Util.fileUpload(file, key));
            photo.setFileName(urls.getPhotoUrl().split("/")[3]);

            BufferedImage firstThumbnail = imageUtil.resizeImage(file, 1);
            String firstThumbnailUrl = s3Util.bufferedImageUpload(firstThumbnail, key, file);
            urls.setThumb_url1(firstThumbnailUrl);

            BufferedImage secondThumbnail = imageUtil.resizeImage(file, 2);
            String secondThumbnailUrl = s3Util.bufferedImageUpload(secondThumbnail, key, file);
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

            //인물분류 API
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try {
                ImageIO.write(firstThumbnail, "jpg", outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] imageBytes = outputStream.toByteArray();

            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("file", imageBytes)
                    .filename(urls.getPhotoUrl())
                    .contentType(MediaType.IMAGE_JPEG);
            bodyBuilder.part("partyId", fileUploadDto.getPartyId());
            bodyBuilder.part("key", key);
            bodyBuilder.part("photoId", photo.getId());

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
            log.info("MongoDB 업데이트 시작");
            //MongoDB 업데이트
            String fileId = fileService.createFile(fileUploadDto.getPartyId(), photo);
            fileService.moveFile(fileId, fileUploadDto.getRootDirId(), fileUploadDto.getTargetDirId());
            //GPT API
            gptService.processGptApiAsync(photo, file);
        }
    }

    public S3Object fileDownload(FileDownloadDto fileDownloadDto, String key) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Photo photo = photoRepository.findById(fileDownloadDto.getFileId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));

        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        if (key == null || key.isEmpty()){
            throw new BusinessLogicException(ErrorCode.SSE_KEY_MISSED);
        }

        String fileName = photo.getFileName();
        SSECustomerKey sseKey = new SSECustomerKey(key);

        S3Object object = null;

        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
            object = amazonS3.getObject(getObjectRequest);
        }catch (Exception e){
            throw new BusinessLogicException(ErrorCode.FILE_NOT_FOUND);
        }

        DownloadLog downloadLog = DownloadLog.create(photo, member);
        downloadLogRepository.save(downloadLog);

        return object;
    }

    public Map<String, List<File>> multiFileDownload(MultiFileDownloadDto multiFileDownloadDto, String key) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<S3Object> objects = new ArrayList<>();
        List<File> files = new ArrayList<>();
        List<Long> fileIds = multiFileDownloadDto.getFileIdList();

        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        if (key == null || key.isEmpty()){
            throw new BusinessLogicException(ErrorCode.SSE_KEY_MISSED);
        }

        SSECustomerKey sseKey = new SSECustomerKey(key);

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

            DownloadLog downloadLog = DownloadLog.create(photo, member);
            downloadLogRepository.save(downloadLog);

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
