package project.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.member.dto.request.SetPasswordDto;
import project.global.result.ResultResponse;

@Tag(name = "MemberController", description = "멤버 api")
public interface MemberDocs {
    @Operation(summary = "내 정보 조회")
    ResponseEntity<ResultResponse> myInfo();

    @Operation(summary = "토큰 갱신")
    ResponseEntity<ResultResponse> reissue(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "로그 아웃")
    ResponseEntity<ResultResponse> logout(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "비밀번호 설정")
    ResponseEntity<ResultResponse> setPassword(@RequestBody SetPasswordDto setPasswordDto);

    @Operation(summary = "그룹 키 조회")
    ResponseEntity<ResultResponse> getKeyGroup(@RequestHeader HttpHeaders headers);

    @Operation(summary = "업로드 디렉토리 변경")
    @PatchMapping("/directory/{partyId}")
    ResponseEntity<ResultResponse> setUploadDirectory(@PathVariable("partyId") Long partyId);

}
