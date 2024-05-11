package project.domain.directory.controller.directory;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import project.domain.directory.dto.request.FileListDto;
import project.domain.directory.dto.response.DirDto;
import project.domain.directory.dto.response.GetChildDirDto;
import project.domain.directory.dto.response.GetDetailFileDto;
import project.domain.directory.dto.response.GetDirDto;
import project.domain.directory.dto.response.GetDirDtov2;
import project.domain.directory.dto.response.GetDirListDto;
import project.domain.directory.dto.response.GetDirThumbUrl2;
import project.domain.directory.service.DirectoryService;
import project.domain.directory.service.FileService;
import project.domain.member.dto.request.CustomOAuth2User;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

@RequestMapping("/directories")
@RestController
@RequiredArgsConstructor
@Slf4j
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
    @GetMapping("")
    public ResponseEntity<ResultResponse> getDirList(
        @PathVariable("userId") Long userId
    ) {
        List<GetDirListDto> response = directoryService.getDirList(userId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_DIRECTORYLIST_SUCCESS, response));
    }

    @Override
    @GetMapping("/{dirId}")
    public ResponseEntity<ResultResponse> getDir(@PathVariable String dirId) {
        GetDirDtov2 response = directoryService.getDirv2(dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_DIRECTORY_SUCCESS, response));
    }

    @Override
    @GetMapping("/{dirId}/main")
    public ResponseEntity<ResultResponse> patchMainDir(
        @AuthenticationPrincipal UserDetails userDetails, String dirId) {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) userDetails;
        Long memberId = customOAuth2User.getId();
        directoryService.patchMainDir(memberId, dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.SET_MAIN_DIRECTORY_SUCCESS));
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
        directoryService.deleteDir(dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.DELETE_DIRECTORY_SUCCESS));
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
        GetDetailFileDto response = fileService.getFile(fileId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_FILE_SUCCESS, response));
    }

    @Override
    @PatchMapping("/{dirId}/files/{fileId}/copy")
    public ResponseEntity<ResultResponse> copyFile(
        @PathVariable(name = "fileId") String fileId,
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId
    ) {
        fileService.copyFile(fileId, fromDirId, toDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_COPY_SUCCESS));
    }

    @Override
    @PatchMapping("/{dirId}/files/copy")
    public ResponseEntity<ResultResponse> copyFileList(
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId,
        @RequestBody FileListDto fileListDto
    ) {
        fileService.copyFileList(fileListDto.getFileIdList(), fromDirId, toDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_COPY_SUCCESS));
    }

    @Override
    @PatchMapping("/{dirId}/files/{fileId}/move")
    public ResponseEntity<ResultResponse> moveFile(
        @PathVariable(name = "fileId") String fileId,
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId
    ) {
        fileService.moveFile(fileId, fromDirId, toDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_MOVE_SUCCESS));
    }

    @Override
    @PatchMapping("/{dirId}/files/move")
    public ResponseEntity<ResultResponse> moveFileList(
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId,
        @RequestBody FileListDto fileListDto
    ) {
        fileService.moveFileList(fileListDto.getFileIdList(), fromDirId, toDirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_MOVE_SUCCESS));
    }

    @Override
    @DeleteMapping("/{dirId}/files/{fileId}")
    public ResponseEntity<ResultResponse> deleteOneFile(
        @PathVariable(name = "dirId") String dirId,
        @PathVariable(name = "fileId") String fileId
    ) {

        fileService.deleteOneFile(fileId, dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_DELETE_SUCCESS));
    }

    @Override
    @DeleteMapping("/{dirId}/files")
    public ResponseEntity<ResultResponse> deleteFileList(
        @PathVariable(name = "dirId") String dirId,
        @RequestBody FileListDto fileListDto
    ) {

        fileService.deleteFileList(fileListDto.getFileIdList(), dirId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.FILE_DELETE_SUCCESS));
    }


    @Override
    @GetMapping("/{dirId}/child")
    public ResponseEntity<ResultResponse> getChildDir(@PathVariable(name = "dirId") String dirId) {
        log.info("come in Controller");
        List<GetChildDirDto> response = directoryService.getChildDir(dirId);
        log.info("controller response = {}", response);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_CHILD_DIR_SUCCESS, response));
    }

    @Override
    @GetMapping("/{dirId}/thumbnail2")
    public ResponseEntity<ResultResponse> getDirThumbUrl2(@PathVariable String dirId) {
        log.info("come in controller");
        List<GetDirThumbUrl2> response = directoryService.getDirThumbUrl2(dirId);
        log.info("controller response = {}", response);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_THUMBNAIL_URL_2_SUCCESS, response));
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
