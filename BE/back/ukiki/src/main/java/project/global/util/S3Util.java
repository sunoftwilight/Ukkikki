package project.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3 amazonS3;

    //커스텀 SSE KEY 인코딩
    public String generateSSEKey(String inputKey) {
        if (inputKey == null || inputKey.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.SSE_KEY_MISSED);
        }

        // Get an instance of SHA-256
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Hash the input text
        byte[] hash = digest.digest(inputKey.getBytes());

        // Encode the hash using Base64
        return Base64.getEncoder().encodeToString(hash);
    }

    //이름 중복 방지를 위해 랜덤으로 생성
    private String changedImageName(String originName) {
        String random = UUID.randomUUID().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmssSSS");
        String ext = originName.substring(originName.lastIndexOf("."));
        return random + LocalDateTime.now().format(formatter) + ext;
    }

    //단일 파일 업로드
    public String fileUpload(MultipartFile file, String key) {
        SSECustomerKey sseKey = new SSECustomerKey(key);
        // 허용할 MIME 타입들 설정 (이미지, 동영상 파일만 허용하는 경우)
        List<String> allowedMimeTypes = List.of("image/jpg", "image/jpeg", "image/png", "image/gif", "video/mp4", "video/webm", "video/ogg", "video/3gpp", "video/x-msvideo", "video/quicktime");

        // 허용되지 않는 MIME 타입의 파일은 처리하지 않음
        String fileContentType = file.getContentType();
        if (!allowedMimeTypes.contains(fileContentType)) {
            log.error("S3 file upload error : " + file.getOriginalFilename());
            throw new IllegalArgumentException("Unsupported file type");
        }

        ObjectMetadata metadata = new ObjectMetadata(); //메타데이터
        metadata.setContentLength(file.getSize()); // 파일 크기 명시
        metadata.setContentType(fileContentType);   // 파일 확장자 명시

        String originName = file.getOriginalFilename(); //원본 이미지 이름
        String changedName = changedImageName(originName); //새로 생성된 이미지 이름

        try {
            //이미지 업로드 전체 읽기 권한 허용, 데이터는 유저키로 암호화, 버킷 정책에 의해 유저키 없이 접근 불가
            amazonS3.putObject(new PutObjectRequest("ukkikki", changedName, file.getInputStream(), metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead).withSSECustomerKey(sseKey));
        } catch (IOException e) {
            log.error("file upload error " + e.getMessage());
            throw new BusinessLogicException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        return amazonS3.getUrl("ukkikki", changedName).toString();
    }

    //썸네일 생성시 버퍼드 이미지를 S3에 업로드 하는 메소드
    public String bufferedImageUpload(BufferedImage bi, String key, MultipartFile file) {
        SSECustomerKey sseKey = new SSECustomerKey(key);
        //바이트 스트림 생성
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //파일 확장자
        String fileContentType = file.getContentType();
        String fileName = file.getOriginalFilename(); //원본 이미지 이름
        String ext = fileName.substring(fileName.lastIndexOf(".")); //확장자

        try {
            ImageIO.write(bi, ext.substring(ext.indexOf(".")+1) ,outputStream);//바이트 스트림에 입력
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMetadata metadata = new ObjectMetadata();//S3 업로드용 메타데이터 생성

        byte[] imageBytes = outputStream.toByteArray();
        metadata.setContentType(fileContentType);//확장자

        InputStream inputStream = new ByteArrayInputStream(imageBytes);//업로드용 인풋스트림 생성

        String originName = file.getOriginalFilename(); //원본 이미지 이름
        String changedName = changedImageName(originName); //새로 생성된 이미지 이름

        //이미지 업로드 전체 읽기 권한 허용, 데이터는 유저키로 암호화, 버킷 정책에 의해 유저키 없이 접근 불가
        try {
            amazonS3.putObject(new PutObjectRequest("ukkikki", changedName, inputStream, metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead).withSSECustomerKey(sseKey));
        }catch (Exception e){
            log.error("file upload error " + e.getMessage());
            throw new BusinessLogicException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        return amazonS3.getUrl("ukkikki", changedName).toString();
    }

    //단일 파일 다운로드
    public S3Object fileDownload(String key, String fileName) {
        SSECustomerKey sseKey = new SSECustomerKey(key);
        GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
        S3Object object = amazonS3.getObject(getObjectRequest);

        return object;
    }

    //S3 사진 삭제
    public void fileDelete(String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest("ukkikki", fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    //sse-c 키값 변경
    public String changeKey(String originKey, String newKey, String fileName){
        SSECustomerKey newSseKey = new SSECustomerKey(newKey);

        S3Object object = fileDownload(originKey, fileName);
        InputStream inputStream = object.getObjectContent();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(object.getObjectMetadata().getContentLength());
        metadata.setContentType(object.getObjectMetadata().getContentType());

        fileDelete(fileName);

        amazonS3.putObject(new PutObjectRequest("ukkikki", fileName, inputStream, metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead).withSSECustomerKey(newSseKey));

        return amazonS3.getUrl("ukkikki", fileName).toString();
    }

    public void fileExpire(String key, String fileName) {
        SSECustomerKey sseKey = new SSECustomerKey(key);

        S3Object object = fileDownload(key, fileName);
        InputStream inputStream = object.getObjectContent();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(object.getObjectMetadata().getContentLength());
        metadata.setContentType(object.getObjectMetadata().getContentType());
        metadata.addUserMetadata("expire-on", LocalDateTime.now().plusMinutes(5).format(DateTimeFormatter.ISO_DATE));

        Tag tag = new Tag("expire", "expire");
        ObjectTagging tagging = new ObjectTagging(List.of(tag));

        fileDelete(fileName);

        amazonS3.putObject(new PutObjectRequest("ukkikki", fileName, inputStream, metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead).withTagging(tagging).withSSECustomerKey(sseKey));
    }

    public void fileUndo(String key, String fileName) {
        SSECustomerKey sseKey = new SSECustomerKey(key);

        S3Object object = fileDownload(key, fileName);
        InputStream inputStream = object.getObjectContent();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(object.getObjectMetadata().getContentLength());
        metadata.setContentType(object.getObjectMetadata().getContentType());
        metadata.addUserMetadata("expire-on", LocalDateTime.now().plusMinutes(5).format(DateTimeFormatter.ISO_DATE));

        fileDelete(fileName);

        amazonS3.putObject(new PutObjectRequest("ukkikki", fileName, inputStream, metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead).withSSECustomerKey(sseKey));
    }

}
