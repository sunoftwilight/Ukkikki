package project.domain.alarm.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.alarm.dto.request.AlarmPageableDto;
import project.domain.alarm.dto.response.AlarmPageDto;
import project.domain.alarm.redis.Alarm;
import project.domain.alarm.redis.AlarmType;

public interface AlarmService {

    SseEmitter createEmitter(UserDetails userDetails);

    void sendAlarm(SseEmitter emitter, Long userId, Alarm data);

    void checkEmitterLive();

    AlarmPageDto getAlarmList(UserDetails userDetails, AlarmPageableDto alarmPageableDto);

    void groupSendAlarm(Long memberId, AlarmType type, Long partyId, Long articleId, Long targetId);

    Alarm createAlarm(AlarmType type, Long partyId, Long articleId, Long targetId, String data);

    SseEmitter findEmitterByUserId(Long memberId);
}
