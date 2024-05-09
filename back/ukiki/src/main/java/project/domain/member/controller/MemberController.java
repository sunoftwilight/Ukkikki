package project.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.dto.response.InfoDto;
import project.domain.member.dto.request.SetPasswordDto;
import project.domain.member.dto.response.KeyGroupDto;
import project.domain.member.service.MemberService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberDocs{

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    // 내 정보 조회
    @Override
    @GetMapping("/info/my")
    public ResponseEntity<ResultResponse> myInfo() {

        // 로그인 성공했을때 저장해두었던 값을들 가져온다.
        try {
            InfoDto infoDTO = memberService.myInfo();
            return ResponseEntity.ok(new ResultResponse(ResultCode.GET_USERINFO_SUCCESS, infoDTO));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<ResultResponse> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키 가져오기
        Cookie[] cookies = request.getCookies();

        // 토큰 발급
        String token = memberService.reissue(cookies);

        response.setHeader("authorization","Bearer " + token);
//        response.addHeader("Access-Control-Expose-Headers", "authorization");

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ResultResponse> logout(HttpServletRequest request, HttpServletResponse response) {

        memberService.logout(request,response);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    @PostMapping("/password")
    public ResponseEntity<ResultResponse> setPassword(@RequestBody SetPasswordDto setPasswordDto) {

        memberService.setPassword(setPasswordDto.getPassword());
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_USERLIST_SUCCESS));

    }

    @Override
    @GetMapping("/mykey")
    public ResponseEntity<ResultResponse> getKeyGroup(@RequestHeader HttpHeaders headers){

        List<KeyGroupDto> response = memberService.getKeyGroup(headers.getFirst("password"));
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_KEYGROUP_SUCCESS, response));

    }

}
