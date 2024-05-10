package project.service;

import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.dto.MQDto;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class MQServiceImpl implements MQService {

    private final ConcurrentLinkedDeque<MQDto> waitLinkedDeque;
    private final ConcurrentLinkedDeque<MQDto>[] workLinkedDeque;

    private final WebClient webClient;

    public MQServiceImpl(ConcurrentLinkedDeque<MQDto> waitLinkedDeque, WebClient webClient) {
        this.waitLinkedDeque = waitLinkedDeque;
        // 배열의 크기를 4로 초기화
        this.workLinkedDeque = new ConcurrentLinkedDeque[2];
        // 배열의 각 요소를 새 ConcurrentLinkedDeque 인스턴스로 초기화
        Arrays.setAll(workLinkedDeque, i -> new ConcurrentLinkedDeque<>());
        this.webClient = webClient;
    }

    @Override
    public void fileUpload(MQDto mqDto) {

        String partyId = mqDto.getPartyId();

        /*
        비어있는 큐 전에
        같은 그룹이 있는지 체크해야 중복이 일어나지 않는다.
         */
        for (ConcurrentLinkedDeque<MQDto> mqDtos : workLinkedDeque) {

            if (!mqDtos.isEmpty()) {
                String peekPartyId = mqDtos.peek().getPartyId();

                if (partyId.equals(peekPartyId)) {
                    mqDtos.add(mqDto);
                    return;
                }
            }

        }

        /*
        작업 중인 같은 그룹이 없다면
        비어 있는 큐를 찾아야한다.
         */
        for(int i=0;i< workLinkedDeque.length;i++){

            if (workLinkedDeque[i].isEmpty()){

                workLinkedDeque[i].add(mqDto);

                fileAiUpload(i);
                return;
            }
        }

        // 비어 있는 큐가 없으면 대기 큐에 넣어준다.
        waitLinkedDeque.add(mqDto);

    }

    @Override
    public void fileAiUpload(int index) {

        MQDto mqDto = workLinkedDeque[index].peek();
        if(mqDto == null)
            return;

        MultipartFile multipartFile = mqDto.getFile(); // mqDto에서 MultipartFile을 가져옴

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            multipartFile.getInputStream().transferTo(baos); // MultipartFile의 내용을 바이트 배열로 변환
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] fileBytes = baos.toByteArray(); // 바이트 배열 저장

    // 바이트 배열을 이용해 MultipartBodyBuilder 생성
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", fileBytes)
                .filename(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                .contentType(MediaType.IMAGE_JPEG);
        bodyBuilder.part("partyId", mqDto.getPartyId());
        bodyBuilder.part("key", mqDto.getKey());
        bodyBuilder.part("index", index);

        // ai 서버로 보내기.
        webClient
                .post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .onErrorResume(e -> {
                    return Mono.fromRunnable(() -> finish(index));
                })
                .subscribe();
    }

    @Override
    public void finish(int index) {
        workLinkedDeque[index].poll();

        if(!workLinkedDeque[index].isEmpty()){
            fileAiUpload(index);
        }else{

            if(!waitLinkedDeque.isEmpty()){

                MQDto mqDto = waitLinkedDeque.poll();

                if(mqDto != null){
                    fileUpload(mqDto);
                }
            }

        }
    }


    @Override
    public void queSize() {
        System.out.println(waitLinkedDeque.size());
        for(int i=0;i<workLinkedDeque.length;i++){
            System.out.println("workLinkedDeque" + i + " = " + workLinkedDeque[i].size());
        }
    }


}
