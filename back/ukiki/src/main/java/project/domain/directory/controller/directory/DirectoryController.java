package project.domain.directory.controller.directory;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.GetFileDto;
import project.domain.directory.service.DirectoryService;
import project.domain.directory.service.FileService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RequestMapping("/directories")
@RestController
@RequiredArgsConstructor
public class DirectoryController implements DirectoryDocs {

    private final DirectoryService directoryService;
    private final FileService fileService;

    @Override
    @PostMapping("/init/{partyId}")
    public ResponseEntity<ResultResponse> initDirPartyTest(@PathVariable Long partyId) {
        DirDto response = directoryService.initDirPartyTest(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_PARTY_SUCCESS, response));
    }

    @Override
    @GetMapping("/{dirId}")
    public ResponseEntity<ResultResponse> getDir(@PathVariable String dirId) {
        GetDirDto response = directoryService.getDir(dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_DIRECTORY_SUCCESS, response));
    }

    @PostMapping("")
    public ResponseEntity<ResultResponse> createDir(@RequestBody CreateDirDto request) {
        GetDirDto response = directoryService.createDir(request);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_DIRECTORY_SUCCESS, response));
    }

    @Override
    @PatchMapping("/{dirId}")
    public ResponseEntity<ResultResponse> moveDir(
        @PathVariable(name = "dirId") String dirId,
        @RequestParam(name = "toDirId") String toDirId
    ) {
        GetDirDto response = directoryService.moveDir(dirId, toDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MOVE_DIRECTORY_SUCCESS, response));
    }

    @Override
    @DeleteMapping("/{dirId}")
    public ResponseEntity<ResultResponse> deleteDir(@PathVariable String dirId) {
        GetDirDto response = directoryService.deleteDir(dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_DIRECTORY_SUCCESS, response));
    }


    @Override
    @PatchMapping("/{dirId}/rename")
    public ResponseEntity<ResultResponse> renameDir(
        @PathVariable(name = "dirId") String dirId,
        @RequestParam(name = "newName") String newName
    ) {
        project.domain.directory.dto.response.RenameDirDto response = directoryService.renameDir(
            dirId, newName);
        return ResponseEntity.ok(new ResultResponse(ResultCode.RENAME_DIRECTORY_SUCCESS, response));
    }

    @Override
    @GetMapping("/{dirId}/files/{fileId}")
    public ResponseEntity<ResultResponse> getFile(
        @PathVariable(name = "dirId") String dirId,
        @PathVariable(name = "fileId") String fileId
    ) {
        GetFileDto response = fileService.getFileDto(fileId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_FILE_SUCCESS, response));
    }

    @Override
    @PatchMapping("/{dirId}/files/{fileId}/copy")
    public ResponseEntity<ResultResponse> copyFile(
        @PathVariable(name = "fileId") String fileId,
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId
    ) {
        GetDirDto response = fileService.copyFile(fileId, fromDirId, toDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_COPY_SUCCESS, response));
    }

    @Override
    @PatchMapping("/{dirId}/files/{fileId}/move")
    public ResponseEntity<ResultResponse> moveFile(
        @PathVariable(name = "fileId") String fileId,
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId
    ) {
        GetDirDto response = fileService.moveFile(fileId, fromDirId, toDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_MOVE_SUCCESS, response));
    }

    @Override
    @DeleteMapping("/{dirId}/files/{fileId}")
    public ResponseEntity<ResultResponse> deleteOneFile(
        @PathVariable(name = "dirId") String dirId,
        @PathVariable(name = "fileId") String fileId
    ) {

        GetDirDto response = fileService.deleteOneFile(fileId, dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_DELETE_SUCCESS, response));
    }

    @Override
    public ResponseEntity<ResultResponse> deleteAllFile(String fileId, String dirId) {
        return null;
    }

    @Override
    public ResponseEntity<ResultResponse> deleteSelectedFile(List<String> fileIdList,
        String dirId) {
        return null;
    }


}
