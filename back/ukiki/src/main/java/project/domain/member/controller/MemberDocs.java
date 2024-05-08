package project.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import project.global.result.ResultResponse;

@Tag(name = "MemberController", description = "멤버 api")
public interface MemberDocs {
    @Operation(summary = "내 정보 조회")
    ResponseEntity<ResultResponse> myInfo();

    @Operation(summary = "토큰 갱신")
    ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response);
}
