package project.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.member.dto.response.InfoDto;
import project.domain.member.service.MemberService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberDocs{

    private final MemberService memberService;

    // 내 정보 조회
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
}
