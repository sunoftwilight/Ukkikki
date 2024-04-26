package project.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        String base64EncodedKey = Base64.getEncoder().encodeToString(hash);

        return base64EncodedKey;
    }

    //이름 중복 방지를 위해 랜덤으로 생성
    private String changedImageName(String originName) {
        String random = UUID.randomUUID().toString();
        return random + originName;
    }

    //단일 파일 업로드
    public String fileUpload(MultipartFile file, SSECustomerKey sseKey) {
        // 허용할 MIME 타입들 설정 (이미지, 동영상 파일만 허용하는 경우)
        List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif", "video/mp4", "video/webm", "video/ogg", "video/3gpp", "video/x-msvideo", "video/quicktime");

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
            log.info("S3 file uploaded : " + originName + " to " + changedName);
        } catch (IOException e) {
            log.error("file upload error " + e.getMessage());
        }

        return amazonS3.getUrl("ukkikki", changedName).toString();
    }

    //썸네일 생성시 버퍼드 이미지를 S3에 업로드 하는 메소드
    public String bufferedImageUpload(BufferedImage bi, SSECustomerKey sseKey, MultipartFile file) {
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
        amazonS3.putObject(new PutObjectRequest("ukkikki", changedName, inputStream, metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead).withSSECustomerKey(sseKey));

        log.info("S3 file uploaded : " + originName + " to " + changedName);

        return amazonS3.getUrl("ukkikki", changedName).toString();
    }

    public S3Object fileDownload(SSECustomerKey sseKey, String fileName) {

        GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
        S3Object object = amazonS3.getObject(getObjectRequest);

        return object;
    }
}
