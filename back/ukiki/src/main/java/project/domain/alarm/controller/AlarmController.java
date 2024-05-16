package project.domain.alarm.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.alarm.dto.request.AlarmPageableDto;
import project.domain.alarm.dto.request.RedirectDto;
import project.domain.alarm.dto.response.AlarmPageDto;
import project.domain.alarm.redis.Alarm;
import project.domain.alarm.redis.AlarmType;
import project.domain.alarm.repository.AlarmRedisRepository;
import project.domain.alarm.service.AlarmService;
import project.domain.member.dto.request.CustomOAuth2User;
import project.domain.member.dto.request.CustomUserDetails;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.io.IOException;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController implements AlarmDocs {

    private final AlarmService alarmService;
    private final AlarmRedisRepository alarmRedisRepository;
    @Override
    @GetMapping(value= "/sub", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subScribe(HttpServletResponse response){
        SseEmitter res = alarmService.createEmitter();
        response.setHeader("X-Accel-Buffering", "no");
        response.setCharacterEncoding("UTF-8");
        return res;
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<ResultResponse> getAlarmList(AlarmPageableDto alarmPageDto) {
        AlarmPageDto res = alarmService.getAlarmList(alarmPageDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_ALARM_SUCCESS, res));
    }

    @Override
    @GetMapping("/redirect-user")
    public void redirectUser(HttpServletResponse response, @RequestParam RedirectDto redirectDto) throws IOException {
        alarmService.checkAlarm(redirectDto.getAlarmId());
        response.sendRedirect("https://k10d202.p.ssafy.io"+redirectDto.getRedirectUrl());
    }

    @GetMapping("/test-alarm")
    public void testAlarm(){

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        SseEmitter asd = alarmService.findEmitterByUserId(userId);
        if(asd != null){
            System.out.println("Emitter IS NOT NULL");
        }

        // 2번 유저  78파티

        Alarm dsa = alarmService.createAlarm(
            AlarmType.CHECK,
            78L,
            1L,
            1L,
            6L,
            "테스트 알람 보내기 입니다."
        );
        dsa.setMemberId(2L);
        alarmRedisRepository.save(dsa);
        alarmService.sendAlarm(asd,userId,dsa);

        dsa = alarmService.createAlarm(
            AlarmType.CHAT,
            78L,
            1L,
            1L,
            6L,
            "채팅 일까 아닐까 "
        );
        dsa.setMemberId(2L);
        alarmRedisRepository.save(dsa);
        alarmService.sendAlarm(asd,userId,dsa);

        dsa = alarmService.createAlarm(
            AlarmType.COMMENT,
            78L,
            1L,
            1L,
            6L,
            "답글 일껄? ??? "
        );
        dsa.setMemberId(2L);
        alarmRedisRepository.save(dsa);
        alarmService.sendAlarm(asd,userId,dsa);

        dsa = alarmService.createAlarm(
            AlarmType.REPLY,
            78L,
            1L,
            1L,
            6L,
            "테스트 알람 보내기 입니다."
        );
        dsa.setMemberId(2L);
        alarmRedisRepository.save(dsa);
        alarmService.sendAlarm(asd,userId,dsa);
//
//
        dsa = alarmService.createAlarm(
            AlarmType.PASSWORD,
            78L,
            1L,
            1L,
            6L,
            "비밀번호 바뀜 ㅅㄱㄹ"
        );
        dsa.setMemberId(2L);
        alarmRedisRepository.save(dsa);
        alarmService.sendAlarm(asd,userId,dsa);
//
//
        dsa = alarmService.createAlarm(
            AlarmType.MENTION,
            78L,
            1L,
            1L,
            6L,
            "@Sun.L 너 짱 잘 나 옴"
        );
        dsa.setMemberId(2L);
        alarmRedisRepository.save(dsa);
        alarmService.sendAlarm(asd,userId,dsa);

        dsa = alarmService.createAlarm(
            AlarmType.MEMO,
            78L,
            "38ca74c4-7797-42b1-a431-38e639a34f532024-05-16T16:08:21.790233970",
            1L,
            6L,
            "우와 짱 잘 나왔다"
        );
        dsa.setMemberId(2L);
        alarmRedisRepository.save(dsa);
        alarmService.sendAlarm(asd,userId,dsa);

        }
}