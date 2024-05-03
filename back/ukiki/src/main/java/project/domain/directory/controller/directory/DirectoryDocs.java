package project.domain.directory.controller.directory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import project.domain.directory.dto.request.CreateDirDto;
import project.global.result.ResultResponse;

public interface DirectoryDocs {

    @PostMapping()
    public ResponseEntity<ResultResponse> initDirPartyTest(@PathVariable Long partyId);

    @GetMapping()
    public ResponseEntity<ResultResponse> getDir(@PathVariable String dirId);

    @Operation(summary = "폴더 생성 요청", description = "Body로 parentDirId, dirName을 받아 현재 위치 폴더 Dto 반환")
    @ApiResponse(responseCode = "201", description = "폴더 생성에 성공하였습니다.")
    @PostMapping("/directory")
    public ResponseEntity<ResultResponse> createDir(@RequestBody CreateDirDto request);

    @PatchMapping()
    public ResponseEntity<ResultResponse> moveDir(
        @PathVariable String dirId,
        @RequestParam String toDirId
    );

    @DeleteMapping()
    public ResponseEntity<ResultResponse> deleteDir(@PathVariable String dirId);

    @PatchMapping
    public ResponseEntity<ResultResponse> renameDir(
        @PathVariable String dirId,
        @RequestParam String newName
    );

    // 사진 조회
    ResponseEntity<ResultResponse> getFile(String dirId, String fileId);

    // 사진 복사
    ResponseEntity<ResultResponse> copyFile(String fileId, String fromDirId, String toDirId);

    // 사진 이동
    ResponseEntity<ResultResponse> moveFile(String fileId, String fromDirId, String toDirId);

    // 단일 사진 삭제
    ResponseEntity<ResultResponse> deleteOneFile(String fileId, String dirId);

    // 전체 사진 삭제
    ResponseEntity<ResultResponse> deleteAllFile(String fileId, String dirId);

    // 선택 사진 삭제
    ResponseEntity<ResultResponse> deleteSelectedFile(List<String> fileIdList, String dirId);
}
