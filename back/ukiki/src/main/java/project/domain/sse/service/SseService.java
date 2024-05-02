package project.domain.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.article.redis.AlarmType;

import java.util.Map;

public interface SseService {

    SseEmitter createEmitter();

    void sendAlarm(SseEmitter emitter, Long userId, AlarmType type, Object data);

    void checkEmitterLive();
}
