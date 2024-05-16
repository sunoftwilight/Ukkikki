package project.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.alarm.dto.request.AlarmPageableDto;
import project.domain.alarm.dto.request.RedirectDto;
import project.global.result.ResultResponse;

import java.io.IOException;

@Tag(name = "AlarmController", description = "알람 api")
public interface AlarmDocs {

    
    @Operation(summary = "alarm 구독하기")
    SseEmitter subScribe(HttpServletResponse response);
    
    
    @Operation(summary = "내 알람 가지고오기")
    ResponseEntity<ResultResponse> getAlarmList(AlarmPageableDto alarmPageDto);

    @Operation(summary = "알람 읽음 처리")
    ResponseEntity<Object> redirectUser(String alarmId) throws IOException;
}
