package project.domain.sse.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class EmitterRepository {

    // ConcurrentHashMap 사용 안하면 time out 했을 때 다른 Thread로 이동할 가능성이 있음
    Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public Map<String, SseEmitter> allEmitter(){
        return emitterMap;
    }

    public void save(String id, SseEmitter emitter){
        emitterMap.put(id, emitter);
    }

    public void deleteById(String id){
        SseEmitter emitter = emitterMap.get(id);
        emitterMap.remove(id);
        emitter.complete();
    }


    public SseEmitter getByUserId(Long userId){
        for (Map.Entry<String, SseEmitter> entry : emitterMap.entrySet()){
            if (entry.getKey().startsWith(String.valueOf(userId))){
                return entry.getValue();
            }
        }
        return null;
    }

    public String getEmitterKeyByUserId(Long userId){
        for (String key : emitterMap.keySet()){
            if (key.startsWith(String.valueOf(userId))){
                return key;
            }
        }
        return null;
    }

    // 내 아이디 생성 emitter 전부 삭제
    public void deleteAllEmitterStartWithId(Long memberId) {
        emitterMap.forEach(
            (key, SseEmitter) -> {
                if (key.startsWith(String.valueOf(memberId))) {
                    SseEmitter.complete();
                    emitterMap.remove(key);
                }
            }
        );
    }

    
}
