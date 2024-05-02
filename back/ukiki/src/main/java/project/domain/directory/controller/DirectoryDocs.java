package project.domain.directory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.request.MoveDirDto;
import project.domain.directory.dto.request.RenameDirDto;
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
    public ResponseEntity<ResultResponse>  moveDir(@RequestBody MoveDirDto request);

    @DeleteMapping()
    public ResponseEntity<ResultResponse> deleteDir(@PathVariable String dirId);

    @PostMapping
    public ResponseEntity<ResultResponse> restoreDir(@PathVariable String deletedDirId);

    @PatchMapping
    public ResponseEntity<ResultResponse> renameDir(@RequestBody RenameDirDto request);
}
