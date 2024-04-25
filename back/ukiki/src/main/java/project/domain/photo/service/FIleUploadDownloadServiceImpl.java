package project.domain.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.entity.Meta;
import project.domain.photo.entity.MetaCode;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.PhotoUrl;
import project.domain.photo.repository.MetaRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.util.FileUtil;
import project.global.util.ImageUtil;
import project.global.util.S3Util;
import project.global.util.gptutil.GptUtil;

import java.io.*;
import java.util.List;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FIleUploadDownloadServiceImpl implements FileUploadDownloadService{

    private final AmazonS3 amazonS3;
    private final PhotoRepository photoRepository;
    // GptUtil
    private final GptUtil gptUtil;
    private final MetaRepository metaRepository;
    private final S3Util s3Util;
    private final ImageUtil imageUtil;
    private final FileUtil fileUtil;



    public void uploadProcess(List<MultipartFile> files, String inputKey, long partyId)
        throws Exception {

        //S3업로드 커스텀 키 생성
        SSECustomerKey sseKey = new SSECustomerKey(s3Util.generateSSEKey(inputKey));
        log.info("sseKey : " + sseKey.getKey());

        for(MultipartFile file : files){
            Photo photo = new Photo();
//            photo.setParty(partyRepository.findById(partyId));
            PhotoUrl urls = new PhotoUrl();
            //S3 파일 업로드 후 저장
            urls.setPhotoUrl(s3Util.fileUpload(file, sseKey));
            photo.setFileName(urls.getPhotoUrl().split("/")[3]);
            urls.setThumb_url1(s3Util.bufferedImageUpload(imageUtil.resizeImage(file, 1), sseKey, file));
            urls.setThumb_url2(s3Util.bufferedImageUpload(imageUtil.resizeImage(file, 2), sseKey, file));
            log.info("urls : " + urls.getPhotoUrl() + ", " + urls.getThumb_url1() + ", " + urls.getThumb_url2()
            + ", " + photo.getFileName());
            // save photo(이부분 알아서 영속성 관리 되도록 변경해야됨)
            photoRepository.save(photo);
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
            //MongoDB 업데이트
        }
    }

    public S3Object fileDownload(String inputKey, long fileId) {
        String fileName = photoRepository.findById(fileId).get().getFileName();
        SSECustomerKey sseKey = new SSECustomerKey(s3Util.generateSSEKey(inputKey));

        GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
        S3Object object = amazonS3.getObject(getObjectRequest);

        return object;
    }

    public Map<String, List<File>> multiFileDownload(String inputKey, List<Long> fileId, String prefix) {
        List<S3Object> objects = new ArrayList<>();
        List<File> files = new ArrayList<>();
        SSECustomerKey sseKey = new SSECustomerKey(s3Util.generateSSEKey(inputKey));
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID();
        File tempDir = new File(tempPath);  // 임시 디렉터리 경로
        log.info("tempDir : " + tempDir.getAbsolutePath());
        tempDir.mkdirs();

        for(long id : fileId){
            String fileName = photoRepository.findById(id).get().getFileName();
            GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
            S3Object object = amazonS3.getObject(getObjectRequest);
            objects.add(object);
        }

        int i = 0;

        for(S3Object object : objects){
            i++;
            InputStream inputStream = object.getObjectContent();
            String type = object.getObjectMetadata().getContentType().split("/")[1];
            File tempFile = null;
            tempFile = new File(tempDir, prefix + i + "." + type);
            log.info("tempFile : " + tempFile.getAbsolutePath());
            tempFile.deleteOnExit();
            fileUtil.copyInputStreamToFile(inputStream, tempFile);
            files.add(tempFile);
        }

        HashMap<String, List<File>> returnMap = new HashMap<>();
        returnMap.put(tempPath, files);
        return returnMap;
    }

}
