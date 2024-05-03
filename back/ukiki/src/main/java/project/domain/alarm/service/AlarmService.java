package project.domain.alarm.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.alarm.dto.request.AlarmPageableDto;
import project.domain.alarm.dto.response.AlarmPageDto;
import project.domain.alarm.redis.Alarm;
import project.domain.alarm.redis.AlarmType;

public interface AlarmService {

    SseEmitter createEmitter();

    void sendAlarm(SseEmitter emitter, Long userId, Alarm data);

    void checkEmitterLive();

    AlarmPageDto getAlarmList(AlarmPageableDto alarmPageableDto);

    void groupSendAlarm(Long memberId, AlarmType type, Long partyId, Long articleId, Long targetId);

    Alarm createAlarm(AlarmType type, Long partyId, Long articleId, Long targetId, String data);
}
