package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.dto.MQDto;

import java.util.Arrays;
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

        for(int i=0;i<workLinkedDeque.length;i++){

            /*
            같은 파티의 작업이 이미 진행중이라면,. 파티내 중복을 없애기 위해
            작업중이 큐에 넣어준다.
             */
            if(!workLinkedDeque[i].isEmpty()){
                String peekPartyId = workLinkedDeque[i].peek().getPartyId();

                if(partyId.equals(peekPartyId)){
                    workLinkedDeque[i].add(mqDto);
                    return;
                }
            }

            // 작업 큐에 비어 있는 곳에 넣어준다.
            if(workLinkedDeque[i].isEmpty()){

                workLinkedDeque[i].add(mqDto);
                fileAiUpload(i);
                return ;
            }

        }

        // 비어 있는 큐가 없으면 대기 큐에 넣어준다.
        waitLinkedDeque.add(mqDto);

    }

    @Override
    public void fileAiUpload(int index) {

        MQDto mqDto = workLinkedDeque[index].peek();
        System.out.println(mqDto);
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", mqDto.getFile());
        bodyBuilder.part("partyId", mqDto.getPartyId());
        bodyBuilder.part("key", mqDto.getKey());

        // ai 서버로 보내기.
//        webClient
//                .post()
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
//                .retrieve()
//                .bodyToMono(String.class)
//                .subscribe();
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
