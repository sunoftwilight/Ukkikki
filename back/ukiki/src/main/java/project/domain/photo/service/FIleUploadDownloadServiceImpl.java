package project.domain.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SSECustomerKey;
import java.awt.Graphics2D;
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
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.entity.Meta;
import project.domain.photo.entity.MetaCode;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.PhotoUrl;
import project.domain.photo.repository.MetaRepository;
import project.domain.photo.repository.PhotoRepository;
import project.global.util.gptutil.GptUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class FIleUploadDownloadServiceImpl implements FileUploadDownloadService{

    private final AmazonS3 amazonS3;
    private final PhotoRepository photoRepository;
    @Value("${cloud.aws.s3.bucketName}")
    private static String bucketName;
    // GptUtil
    private final GptUtil gptUtil;
    private final MetaRepository metaRepository;

    //커스텀 SSE KEY 인코딩
    private String generateSSEKey(String inputKey) {
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

    @Override
    public String fileUpload(MultipartFile file, SSECustomerKey sseKey) {
        // 허용할 MIME 타입들 설정 (이미지, 동영상 파일만 허용하는 경우)
        List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "image/gif", "video/mp4", "video/webm", "video/ogg", "video/3gpp", "video/x-msvideo", "video/quicktime");

        // 허용되지 않는 MIME 타입의 파일은 처리하지 않음
        String fileContentType = file.getContentType();
        if (!allowedMimeTypes.contains(fileContentType)) {
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

        log.info("bi length: " + bi.getWidth() + ", " + bi.getHeight());

        try {
            ImageIO.write(bi, ext.substring(ext.indexOf(".")+1) ,outputStream);//바이트 스트림에 입력
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMetadata metadata = new ObjectMetadata();//S3 업로드용 메타데이터 생성

        byte[] imageBytes = outputStream.toByteArray();
        metadata.setContentLength(imageBytes.length);//이미지 크기
        log.info("byte length : "+imageBytes.length);
        metadata.setContentType(fileContentType);//확장자

        InputStream inputStream = new ByteArrayInputStream(imageBytes);//업로드용 인풋스트림 생성

        String originName = file.getOriginalFilename(); //원본 이미지 이름
        String changedName = changedImageName(originName); //새로 생성된 이미지 이름

        //이미지 업로드 전체 읽기 권한 허용, 데이터는 유저키로 암호화, 버킷 정책에 의해 유저키 없이 접근 불가
        amazonS3.putObject(new PutObjectRequest("ukkikki", changedName, inputStream, metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead).withSSECustomerKey(sseKey));

        return amazonS3.getUrl("ukkikki", changedName).toString();
    }

    public void updateDatabase(int partyId, List<String> urls){
        //todo : 사진 업로드 후 데이터 베이스 업데이트 작업 수행
    }

    public BufferedImage resizeImage(MultipartFile file, int thumbnailNumber) {
        //todo : 썸네일 생성
        int width = 0;
        int height = 0;
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(thumbnailNumber == 1){
            width = inputImage.getWidth() / 2;
            height = inputImage.getHeight() / 2;
        }
        if(thumbnailNumber == 2){
            width = inputImage.getWidth() / 4;
            height = inputImage.getHeight() / 4;
        }

        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(inputImage, 0, 0, width, height, null);
        graphics2D.dispose();

        return outputImage;
    }

    public void uploadProcess(List<MultipartFile> files, String inputKey, long partyId)
        throws Exception {

        //S3업로드 커스텀 키 생성
        SSECustomerKey sseKey = new SSECustomerKey(generateSSEKey(inputKey));
        log.info("sseKey : " + sseKey.getKey());

        for(MultipartFile file : files){
            Photo photo = new Photo();
//            photo.setParty(partyRepository.findById(partyId));
            PhotoUrl urls = new PhotoUrl();
            //S3 파일 업로드 후 저장
            urls.setPhotoUrl(fileUpload(file, sseKey));
            photo.setFileName(urls.getPhotoUrl().split("/")[3]);
            urls.setThumb_url1(bufferedImageUpload(resizeImage(file, 1), sseKey, file));
            urls.setThumb_url2(bufferedImageUpload(resizeImage(file, 2), sseKey, file));
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
        String fileName = photoRepository.findById(fileId).getFileName();
        SSECustomerKey sseKey = new SSECustomerKey(generateSSEKey(inputKey));

        GetObjectRequest getObjectRequest = new GetObjectRequest("ukkikki", fileName).withSSECustomerKey(sseKey);
        S3Object object = amazonS3.getObject(getObjectRequest);

        log.info("object : " + object.getKey());
        log.info("object : " + object.getObjectMetadata().getContentType());
        log.info("object : " + object.getObjectMetadata().getContentLength());

        return object;
    }
}
