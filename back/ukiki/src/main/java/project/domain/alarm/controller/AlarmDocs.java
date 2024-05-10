package project.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.alarm.dto.request.AlarmPageableDto;
import project.global.result.ResultResponse;
@Tag(name = "AlarmController", description = "알람 api")
public interface AlarmDocs {

    
    @Operation(summary = "alarm 구독하기")
    public SseEmitter subScribe(HttpServletResponse response);
    
    
    @Operation(summary = "내 알람 가지고오기")
    public ResponseEntity<ResultResponse> getAlarmList(AlarmPageableDto alarmPageDto);
}
