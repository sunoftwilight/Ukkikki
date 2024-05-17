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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class MQServiceImpl implements MQService {

    private final BlockingQueue<MQDto> evenQueue;
    private final BlockingQueue<MQDto> oddQueue;

    private final WebClient webClient;

    @Autowired
    public MQServiceImpl(BlockingQueue<MQDto> evenQueue, BlockingQueue<MQDto> oddQueue, WebClient webClient) {
        this.evenQueue = evenQueue;
        this.oddQueue = oddQueue;
        this.webClient = webClient;
    }

    @Override
    public void fileUpload(MQDto mqDto) {
        Long partyId = mqDto.getPartyId();
        log.info("업로드 짝수 큐 : " + evenQueue.size());
        log.info("업로드 홀수 큐 : " + oddQueue.size());

        switch ((int) (partyId % 2)) {
            case 0:
                evenQueue.offer(mqDto);
                if (evenQueue.size() == 1) {
                    fileAiUpload(0);
                }
                break;
            case 1:
                oddQueue.offer(mqDto);
                if (oddQueue.size() == 1) {
                    fileAiUpload(1);
                }
                break;
        }
    }

    @Override
    public void fileAiUpload(int index) {
        log.info("index : " + index + "의 작업이 시작됩니다.");

        log.info("짝수 큐 : " + evenQueue.size());
        log.info("홀수 큐 : " + oddQueue.size());

        MQDto mqDto;
        try {
            mqDto = index == 0 ? evenQueue.take() : oddQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

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
        // 인덱스에 따라 다음 작업을 실행합니다.
        if (index == 0) {
            if (!evenQueue.isEmpty()) {
                fileAiUpload(index);
            }
        } else {
            if (!oddQueue.isEmpty()) {
                fileAiUpload(index);
            }
        }
    }
}
