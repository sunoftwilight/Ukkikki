package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.dto.MQDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
@Slf4j
public class MQServiceImpl implements MQService {

    private final ConcurrentLinkedDeque<MQDto> evenLinkedDeque;
    private final ConcurrentLinkedDeque<MQDto> oddLinkedDeque;

    private final WebClient webClient;
    @Autowired
    public MQServiceImpl(ConcurrentLinkedDeque<MQDto> evenLinkedDeque, ConcurrentLinkedDeque<MQDto> oddLinkedDeque,WebClient webClient) {
        this.evenLinkedDeque = evenLinkedDeque;
        this.oddLinkedDeque = oddLinkedDeque;
        this.webClient = webClient;
    }

    @Override
    public void fileUpload(MQDto mqDto) {

        Long partyId = mqDto.getPartyId();

        /*
            파일 업로드
         */
        //기본 경로
        String uploadDir = "/home/ubuntu/mq/file/" + partyId + "/";

        //폴더가 없으면 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 지정된 디렉토리에 파일 저장
        String fileName = getFileName(mqDto.getFile().getOriginalFilename());

        try {
            mqDto.getFile().transferTo(new File(uploadDir + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*
        홀수 짝수 구분 후 큐에 넣어준다.
        만약 큐가 비어있었다면
        바로 실행할 수 있게 해준다.
         */
        switch ((int) (partyId%2)){
            case 0:
                evenLinkedDeque.offerLast(mqDto);
                if(evenLinkedDeque.size() == 1){
                    fileAiUpload(0);
                }
                break;
            case 1:
                oddLinkedDeque.offerLast(mqDto);
                if(oddLinkedDeque.size() == 1){
                    fileAiUpload(1);
                }
                break;
        }


    }

    @Override
    public void fileAiUpload(int index) {

        log.info("index : " + index + "의 작업이 시작됩니다.");

        MQDto mqDto = index == 0 ? evenLinkedDeque.peekFirst() : oddLinkedDeque.peekFirst();

        if(mqDto == null)
            return;

        MultipartFile multipartFile = mqDto.getFile(); // mqDto에서 MultipartFile을 가져옴

        // 기본 경로 및 파일 이름
        String uploadDir = "/home/ubuntu/mq/file/" + mqDto.getPartyId() + "/";
        String fileName = getFileName(mqDto.getFile().getOriginalFilename());
        String fullPath = uploadDir + fileName; // 전체 파일 경로

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        byte[] fileBytes = null;


        try {

            fis = new FileInputStream(fullPath); // 저장된 파일로부터 FileInputStream 생성
            byte[] buffer = new byte[1024]; // 1KB 버퍼
            int length;
            while ((length = fis.read(buffer)) != -1) { // 파일 끝까지 읽기
                baos.write(buffer, 0, length); // ByteArrayOutputStream에 쓰기
            }

            // 바이트 배열로 변환
            fileBytes = baos.toByteArray();

        } catch (IOException e) {

            log.error("Error occurred during file reading. File path: " + fullPath, e);
            // 실패 시 다음 작업으로 넘어간다.
            finish(index);
            return;

        } finally {

            try {
                if (fis != null) fis.close(); // FileInputStream 닫기
                baos.close(); // ByteArrayOutputStream 닫기
            } catch (IOException e) {
                log.error("Error occurred during stream closing.", e);
            }

        }


        // 바이트 배열을 이용해 MultipartBodyBuilder 생성
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", fileBytes)
                .filename(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                .contentType(MediaType.IMAGE_JPEG);
        bodyBuilder.part("partyId", mqDto.getPartyId());
        bodyBuilder.part("key", mqDto.getKey());
        bodyBuilder.part("photoId", mqDto.getPhotoId());
        bodyBuilder.part("index", index);

        // ai 서버로 보내기.
        webClient
                .post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        (res) -> {
                            log.info("index : " + index + "의 작업이 완료되었습니다.");
                            finish(index);
                        },
                        (err) -> {
                            log.info("index : " + index + "의 작업이 완료되었습니다.");
                            finish(index);
                        }
                );
    }

    @Override
    public void finish(int index) {
        log.info("index의 작업을 제거합니다");

        // 인덱스에 따라 값을 제거해준다.
        MQDto mqDto = index == 0 ? evenLinkedDeque.poll() : oddLinkedDeque.poll();
        
        // 파일 경로
        String uploadDir = "/home/ubuntu/mq/file/" + mqDto.getPartyId() + "/";
        String fileName = getFileName(mqDto.getFile().getOriginalFilename());
        String fullPath = uploadDir + fileName; // 전체 파일 경로
        
        // 저장했던 파일 삭제
        File file = new File(fullPath);
        file.delete();

        // 다음 작업 고
        if (index == 0) {

            // 큐에 값이 남아있다면 반복한다.
            if(!evenLinkedDeque.isEmpty()){
                fileAiUpload(index);
            }

        } else {

            if(!oddLinkedDeque.isEmpty()){
                fileAiUpload(index);
            }

        }

    }

    public String getFileName(String name) {
        return Objects.requireNonNull(name).replaceAll("[^a-zA-Z0-9.]", "");
    }

}
