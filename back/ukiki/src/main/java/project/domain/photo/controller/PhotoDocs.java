package project.domain.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.domain.photo.dto.request.MemoDto;
import project.domain.photo.dto.request.MemoModifyDto;
import project.global.result.ResultResponse;

@Tag(name = "PhotoController", description = "사진 api")
public interface PhotoDocs {

    @Operation(summary = "메모 조회")
    @ApiResponse(responseCode = "200", description = "메모 조회에 성공하였습니다.")
    @GetMapping("/memo")
    ResponseEntity<ResultResponse> memo(String fileId);

    @Operation(summary = "메모 등록")
    @ApiResponse(responseCode = "200", description = "메모 등록에 성공하였습니다.")
    @PostMapping("/memo/create")
    ResponseEntity<ResultResponse> memoCreate(MemoDto memoDto);

    @Operation(summary = "메모 수정")
    @ApiResponse(responseCode = "200", description = "메모 수정에 성공하였습니다.")
    @PostMapping("/memo/modify")
    ResponseEntity<ResultResponse> memoModify(MemoModifyDto memoModifyDto);

    @Operation(summary = "메모 삭제")
    @ApiResponse(responseCode = "200", description = "메모를 정상적으로 삭제하였습니다.")
    @DeleteMapping("/memo/delete")
    ResponseEntity<ResultResponse> memoDelete(Long memoId);

    @Operation(summary = "사진 좋아요 등록 요청", description = "PathVariable로 fileId 받아 좋아요 등록")
    @ApiResponse(responseCode = "200", description = "좋아요에 성공하였습니다.")
    @PostMapping("/{fileId}/likes")
    ResponseEntity<ResultResponse> likesCreate(String fileId);

    @Operation(summary = "사진 좋아요 취소 요청", description = "PathVariable로 fileId 받아 좋아요 취소")
    @ApiResponse(responseCode = "204", description = "좋아요 취소에 성공하였습니다.")
    @DeleteMapping("/{fileId}/likes")
    ResponseEntity<ResultResponse> likesDelete(String fileId);
}
