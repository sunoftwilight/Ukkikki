package project.domain.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.global.result.ResultResponse;

@Tag(name = "PhotoController", description = "사진 api")
public interface PhotoDocs {

    @Operation(summary = "메모 조회")
    @ApiResponse(responseCode = "200", description = "메모 조회에 성공하였습니다.")
    @GetMapping("memo/{fileId}")
    ResponseEntity<ResultResponse> memo(@PathVariable("fileId") String fileId);

    @Operation(summary = "메모 등록")
    @ApiResponse(responseCode = "200", description = "메모 등록에 성공하였습니다.")
    @PostMapping("/memo/create/{fileId}")
    ResponseEntity<ResultResponse> memoCreate(@PathVariable("fileId") String fileId, @RequestBody String content);

    @Operation(summary = "메모 수정")
    @ApiResponse(responseCode = "200", description = "메모 수정에 성공하였습니다.")
    @PostMapping("/memo/modify/{memoId}")
    ResponseEntity<ResultResponse> memoModify(@PathVariable("memoId") Long memoId, @RequestBody String content);

    @Operation(summary = "메모 삭제")
    @ApiResponse(responseCode = "200", description = "메모를 정상적으로 삭제하였습니다.")
    @DeleteMapping("/memo/delete/{memoId}")
    ResponseEntity<ResultResponse> memoDelete(@PathVariable("memoId") Long memoId);

    @Operation(summary = "사진 좋아요 등록 요청", description = "PathVariable로 fileId 받아 좋아요 등록")
    @ApiResponse(responseCode = "200", description = "좋아요에 성공하였습니다.")
    @PostMapping("/likes/{fileId}")
    ResponseEntity<ResultResponse> likesCreate(@PathVariable("fileId") String fileId);

    @Operation(summary = "사진 좋아요 취소 요청", description = "PathVariable로 fileId 받아 좋아요 취소")
    @ApiResponse(responseCode = "204", description = "좋아요 취소에 성공하였습니다.")
    @DeleteMapping("/likes/{fileId}")
    ResponseEntity<ResultResponse> likesDelete(@PathVariable("fileId") String fileId);
}
