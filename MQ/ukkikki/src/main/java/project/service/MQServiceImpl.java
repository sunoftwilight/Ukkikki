package project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.dto.MQDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
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

        log.info("업로드 짝수 큐 : " + evenLinkedDeque.size());
        log.info("업로드 홀수 큐 : " + oddLinkedDeque.size());
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

        log.info("짝수 큐 : " + evenLinkedDeque.size());
        log.info("홀수 큐 : " + oddLinkedDeque.size());

        MQDto mqDto = index == 0 ? evenLinkedDeque.peekFirst() : oddLinkedDeque.peekFirst();

        if(mqDto == null)
            return;

        MultipartFile multipartFile = mqDto.getFile(); // mqDto에서 MultipartFile을 가져옴

        
        log.info("파일 정보 : " + multipartFile.getOriginalFilename());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            multipartFile.getInputStream().transferTo(baos); // MultipartFile의 내용을 바이트 배열로 변환
        } catch (IOException e) {
                log.error("Error occurred during file conversion. File path: " + multipartFile.getOriginalFilename(), e);
        }
        
        // 바이트 배열 저장
        byte[] fileBytes = baos.toByteArray();
        
        // 바이트 배열을 이용해 MultipartBodyBuilder 생성
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", fileBytes)
                .filename(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                .contentType(MediaType.IMAGE_JPEG);
        bodyBuilder.part("partyId", mqDto.getPartyId());
        bodyBuilder.part("key", mqDto.getKey());
        bodyBuilder.part("photoId", mqDto.getPhotoId());
        bodyBuilder.part("index", index);

        log.info("file byte : " + Arrays.toString(fileBytes));

        // ai 서버로 보내기.
        webClient
                .post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        (response) -> {
                            try {
                                // Jackson ObjectMapper를 사용하여 JSON 문자열 파싱
                                ObjectMapper objectMapper = new ObjectMapper();
                                JsonNode rootNode = objectMapper.readTree(response);

                                // "index" 필드 값 추출
                                String responseIndex = rootNode.path("index").asText();

                                // 추출한 index 값을 사용하여 원하는 작업 수행
                                finish(Integer.parseInt(responseIndex));

                            } catch (Exception e) {
                                finish(index);
                            }
                        },
                        error -> finish(index),
                        () -> log.info("index : " + index + "의 작업이 완료되었습니다.")
                );
    }

    @Override
    public void finish(int index) {
        log.info("index의 작업을 제거합니다");
        // 인덱스에 따라 값을 제거해준다.
        if (index == 0) {
            evenLinkedDeque.pollFirst();

            // 큐에 값이 남아있다면 반복한다.
            if(!evenLinkedDeque.isEmpty()){
                fileAiUpload(index);
            }

        } else {
            oddLinkedDeque.pollFirst();

            if(!oddLinkedDeque.isEmpty()){
                fileAiUpload(index);
            }

        }

    }


//    @Override
//    public void queSize() {
//        System.out.println(waitLinkedDeque.size());
//        for(int i=0;i<workLinkedDeque.length;i++){
//            System.out.println("workLinkedDeque" + i + " = " + workLinkedDeque[i].size());
//        }
//    }


}
