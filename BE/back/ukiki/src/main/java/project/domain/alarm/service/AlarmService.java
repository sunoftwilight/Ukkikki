package project.domain.alarm.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
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

    void groupSendAlarm(Alarm alarm, Long senderId);

    Alarm createAlarm(AlarmType type, Long partyId, Object contentsId, Object targetId, Long writerId, String data);

    SseEmitter findEmitterByUserId(Long memberId);

    void checkAlarm(String alarmId);
}
