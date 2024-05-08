package project.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.alarm.dto.request.AlarmPageableDto;
import project.domain.alarm.dto.response.AlarmPageDto;
import project.domain.alarm.redis.Alarm;
import project.domain.alarm.redis.AlarmType;
import project.domain.alarm.service.AlarmService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController implements AlarmDocs {

    private final AlarmService alarmService;

    @Override
    @GetMapping(value= "/sub", produces = "text/event-stream")
    public SseEmitter subScribe(){
        SseEmitter res = alarmService.createEmitter();
        return res;
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<ResultResponse> getAlarmList(@ModelAttribute AlarmPageableDto alarmPageDto) {
        AlarmPageDto res = alarmService.getAlarmList(alarmPageDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_ALARM_SUCCESS, res));
    }


}