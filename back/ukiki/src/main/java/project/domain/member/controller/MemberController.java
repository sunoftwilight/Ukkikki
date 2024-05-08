package project.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.domain.member.dto.request.CustomOAuth2User;
import project.domain.member.dto.request.SetPasswordDto;
import project.domain.member.dto.response.KeyGroupDto;
import project.domain.member.service.MemberService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.util.List;

@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/password")
    public ResponseEntity<ResultResponse> setPassword(@RequestBody SetPasswordDto setPasswordDto, @AuthenticationPrincipal UserDetails userDetails){
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) userDetails;
        //유저 아이디 필요함
        Long userId = 1L;
        memberService.setPassword(setPasswordDto.getPassword(), userId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_USERLIST_SUCCESS));
    }

    //여기서 password를 쿼리 파라메터로 받는건 말도 안되는 짓이잖아 내일 하자
    @GetMapping("/mykey")
    public ResponseEntity<ResultResponse> getKeyGroup(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = 1L;
        String password = "임시";
        List<KeyGroupDto> response = memberService.getKeyGroup(userId, password);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_KEYGROUP_SUCCESS, response));
    }
}
