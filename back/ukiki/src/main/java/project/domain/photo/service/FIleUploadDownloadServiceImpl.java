package project.domain.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.security.MessageDigest;

@Slf4j
@Service
@RequiredArgsConstructor
public class FIleUploadDownloadServiceImpl implements FileUploadDownloadService{

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucketName}")
    private static String bucketName;

    //MD5 암호화와 Base64 인코딩
    public static String md5AndBase64(String plainText) throws NoSuchAlgorithmException {

        //MessageDigest 인스턴스 생성(MD5)
        MessageDigest md = MessageDigest.getInstance("MD5");

        //해쉬값 업데이트
        md.update(plainText.getBytes());

        //Byte To Base64 String
        return DatatypeConverter.printBase64Binary(md.digest());
    }

    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec("01234567890123456789012345678901".getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec("01234567890123456789012345678901".substring(0, 16).getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        String encryptFinal = Base64.getEncoder().encodeToString(encrypted);
        encryptFinal = encryptFinal.replace("=", "A");
        return encryptFinal.concat(encryptFinal);
    }

    //이름 중복 방지를 위해 랜덤으로 생성
    private String changedImageName(String originName) {
        String random = UUID.randomUUID().toString();
        return random + originName;
    }

    @Override
    public void fileUpload(List<MultipartFile> files, String text) throws Exception {
        // 허용할 MIME 타입들 설정 (이미지, 동영상 파일만 허용하는 경우)
        List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif", "video/mp4", "video/webm", "video/ogg", "video/3gpp", "video/x-msvideo", "video/quicktime");

        // 업로드한 파일의 업로드 경로를 담을 리스트
        List<String> Urls = new ArrayList<>();

        KeyGenerator KEY_GENERATOR = KeyGenerator.getInstance("AES");
        KEY_GENERATOR.init(256, new SecureRandom());
        SSECustomerKey SSE_KEY = new SSECustomerKey(KEY_GENERATOR.generateKey());
        log.info("SSE_KEY : " + SSE_KEY.getKey());
        log.info("SSE_KEY Length : " + SSE_KEY.getKey().getBytes().length);
        log.info("SSE_KEY Length : " + SSE_KEY.getKey().length());
        log.info("SSE_KEY Algorithm : " + SSE_KEY.getAlgorithm());
        log.info("SSE_KEY Algorithm : " + SSE_KEY.getMd5());
        for (MultipartFile file : files) {

            // 허용되지 않는 MIME 타입의 파일은 처리하지 않음
            String fileContentType = file.getContentType();
            if (!allowedMimeTypes.contains(fileContentType)) {
                throw new IllegalArgumentException("Unsupported file type");
            }

            ObjectMetadata metadata = new ObjectMetadata(); //메타데이터

            metadata.setContentLength(file.getSize()); // 파일 크기 명시
            metadata.setContentType(fileContentType);   // 파일 확장자 명시
//            metadata.setHeader("x-amz-server-side-encryption-customer-key", encrypt(text));
//            log.info("key : " + encrypt(text));
//            metadata.setHeader("x-amz-server-side-encryption-customer-key-MD5", md5AndBase64(text));

            String originName = file.getOriginalFilename(); //원본 이미지 이름
            String changedName = changedImageName(originName); //새로 생성된 이미지 이름
//            String ext = originName.substring(originName.lastIndexOf(".")); //확장자

            try {
                PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
                        "ukkikki", changedName, file.getInputStream(), metadata
                ).withSSECustomerKey(SSE_KEY));
            } catch (IOException e) {
                log.error("file upload error " + e.getMessage());
            }
            Urls.add(amazonS3.getUrl(bucketName, changedName).toString());
        }
    }
}
