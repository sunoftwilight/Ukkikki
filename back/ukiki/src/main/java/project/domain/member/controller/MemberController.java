package project.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.member.dto.response.InfoDto;
import project.domain.member.service.MemberService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberDocs{

    private final MemberService memberService;

    // 내 정보 조회
    @Override
    @GetMapping("/info/my")
    public ResponseEntity<ResultResponse> myInfo() {

        // 로그인 성공했을때 저장해두었던 값을들 가져온다.
        try {
            InfoDto infoDTO = memberService.myInfo();
            System.out.println(infoDTO);
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

        response.setHeader("access",token);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ResultResponse> logout(HttpServletRequest request, HttpServletResponse response) {

        memberService.logout(request,response);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
