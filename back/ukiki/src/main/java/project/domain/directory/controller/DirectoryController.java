package project.domain.directory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.request.MoveDirDto;
import project.domain.directory.dto.request.RenameDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.service.DirectoryService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RequestMapping("/directory")
@RestController
@RequiredArgsConstructor
public class DirectoryController implements DirectoryDocs{

    private final DirectoryService directoryService;

    @Override
    @PostMapping("/init/{partyId}")
    public ResponseEntity<ResultResponse> initDir(@PathVariable Long partyId) {
        DirDto response = directoryService.initDirParty(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_PARTY_SUCCESS, response));
    }

    @Override
    @GetMapping("/{dirId}")
    public ResponseEntity<ResultResponse> getDir(@PathVariable String dirId) {
        DirDto response = directoryService.getDir(dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_DIRECTORY_SUCCESS, response));
    }

    @PostMapping()
    public ResponseEntity<ResultResponse> createDir(@RequestBody CreateDirDto request) {
        DirDto response = directoryService.createDir(request);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_DIRECTORY_SUCCESS, response));
    }

    @Override
    @PatchMapping("/move")
    public ResponseEntity<ResultResponse> moveDir(@RequestBody MoveDirDto request) {
        DirDto response = directoryService.moveDir(request);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MOVE_DIRECTORY_SUCCESS, response));
    }

    @Override
    @DeleteMapping("/{dirId}")
    public ResponseEntity<ResultResponse> deleteDir(@PathVariable String dirId) {
        DirDto response = directoryService.deleteDir(dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_DIRECTORY_SUCCESS, response));
    }

    @Override
    @PostMapping("/restore/{deletedDirId}")
    public ResponseEntity<ResultResponse> restoreDir(@PathVariable String deletedDirId) {
        DirDto response = directoryService.restoreDir(deletedDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.RESTORED_DIRECTORY_SUCCESS, response));
    }

    @Override
    @PatchMapping("/rename")
    public ResponseEntity<ResultResponse> renameDir(@RequestBody RenameDirDto request) {
        project.domain.directory.dto.response.RenameDirDto response = directoryService.renameDir(request);
        return ResponseEntity.ok(new ResultResponse(ResultCode.RENAME_DIRECTORY_SUCCESS, response));
    }
}
