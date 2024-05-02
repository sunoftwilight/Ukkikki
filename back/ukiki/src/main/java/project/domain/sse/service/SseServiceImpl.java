package project.domain.sse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.article.redis.AlarmType;
import project.domain.sse.repository.EmitterRepository;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Component
public class SseServiceImpl implements SseService{

    private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 60;
    private final EmitterRepository emitterRepository;

    @Override
    public SseEmitter createEmitter(){
        // TODO 유저 아이디 받아와야함
        Long userId = 1L;
        // 생성했던 emitter 삭제
        emitterRepository.deleteAllEmitterStartWithId(userId);

        String emitterId = makeEmitterId(userId);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId, sseEmitter);

        // 자동삭제 설정
        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 연결유지 메시지 보내기
        sendAlarm(sseEmitter, userId, AlarmType.CHECK, "");

        return sseEmitter;
    }

    // 유저 아이디로 emitter 조회 / not exist -> return null;
    public SseEmitter findEmitterByUserId(Long userId){
        return emitterRepository.getByUserId(userId);
    }

    /**
     * @param emitter   유저와 연결되있는 SSE : findEmitterByUserId() 함수에서 얻을 수 있다.
     * @param userId        Member id
     * @param type      보낸 메시지의 타입 : ex> KEY : 임호변경 등등  enum으로 관리
     * @param data      메시지에 담기는 데이타 : 이동하는데 필요한 데이터가 들어간다
     */
    @Override
    public void sendAlarm(SseEmitter emitter, Long userId, AlarmType type, Object data){
        try{
            emitter.send(SseEmitter.event()
                .id(makeEmitterId(userId))
                .name(String.valueOf(type))
                .data(data)
            );
        }catch (IOException e){
            String emitterKey = emitterRepository.getEmitterKeyByUserId(userId);
            emitterRepository.deleteById(emitterKey);
        }
    }

    // 모든 SseEmitter의 생존 여부를 판단해주는 함수
    @Override
    public void checkEmitterLive() {
        Map<String, SseEmitter> allEmitter = emitterRepository.allEmitter();
        for (Map.Entry<String, SseEmitter> entry : allEmitter.entrySet()) {
            sendAlarm(entry.getValue(), Long.valueOf(entry.getKey().split("_")[0]), AlarmType.CHECK, "");
        }
    }



    // Emitter Id를 만들어 준다
    public String makeEmitterId(Long userId){
        return userId + "_" + System.currentTimeMillis();
    }

}
