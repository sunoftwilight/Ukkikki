package project.domain.directory.controller.directory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import project.domain.directory.dto.request.CreateDirDto;
import project.domain.directory.dto.request.FileListDto;
import project.domain.directory.dto.response.GetChildDirDto;
import project.domain.directory.dto.response.GetDetailFileDto;
import project.domain.directory.dto.response.GetDirDtov2;
import project.domain.directory.dto.response.GetDirListDto;
import project.domain.directory.dto.response.GetDirThumbUrl2;
import project.global.result.ResultResponse;

@Tag(name ="공유 앨범 폴더및 사진 조작 관련(휴지통 아님) Controller", description = "폴더 조작 및 사진 파일 조작 API ")
public interface DirectoryDocs {

    @Operation(summary = "특정 파티에 공유 앨범 폴더 생성 요청(초기 공유 앨범 생성용도)", description = "PathVariable로 파티id를 받아 생성된 폴더 정보를 반환")
    @ApiResponse(responseCode = "201", description = "그룹 생성에 성공하였습니다.")
    @PostMapping("/init/{partyId}")
    public ResponseEntity<ResultResponse> initDirPartyTest(@PathVariable Long partyId);

    @Operation(summary = "해당 유저의 보유 그룹 리스트 조회 요청(회원 로직이 완료되면 pathVariable 제외 예정)", description = "PathVariable로 userId를 받아 해당 그룹 리스트 반환")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "0", description = "응답 양식",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResultResponse.class)
            )
        ),
        @ApiResponse(responseCode = "200", description = "data 내용 아래의 DTO가 List에 싸여옵니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetDirListDto.class)
            ))
    })
    @GetMapping("")
    public ResponseEntity<ResultResponse> getDirList(Long userId);

    @Operation(summary = "폴더 조회 요청", description = "PathVariable로 dirId를 받아 해당 폴더 정보를 반환")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "0", description = "응답 양식",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResultResponse.class)
            )
        ),
        @ApiResponse(responseCode = "200", description = "data 내용 dir과 file 타입에 따라 속성이 비어있을 수 있습니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetDirDtov2.class)
            ))
    })
    @GetMapping("/{dirId}")
    public ResponseEntity<ResultResponse> getDir(@PathVariable String dirId);

    @Operation(summary = "메인 폴더 설정 변경 요청", description = "PathVariable로 dirId를 받아 유저의 메인 폴더 속성 변경")
    @ApiResponse(responseCode = "200", description = "메인 폴더 설정 변경에 성공하였습니다.")
    @GetMapping("/{dirId}/main")
    public ResponseEntity<ResultResponse> patchMainDir(
        @AuthenticationPrincipal UserDetails userDetails, @PathVariable String dirId);

    @Operation(summary = "자식폴더 생성 요청(선결 조건으로 부모 폴더가 필요합니다./party/create를 통해 파티를 먼저 생성하세요)", description = "Body로 parentDirId(부모폴더 ID), dirName(생성 할 폴더명)을 받아 부모폴더의 정보를 반환")
    @ApiResponse(responseCode = "201", description = "폴더 생성에 성공하였습니다.")
    @PostMapping("")
    public ResponseEntity<ResultResponse> createDir(@RequestBody CreateDirDto request);

    @Operation(summary = "폴더 위치 변경 요청", description = "PathVariable로 dirId(이동 대상이 되는 폴더Id), RequestParam으로 toDirId(목적지가 되는 폴더 Id)를 받아 폴더 위치 변경 ")
    @ApiResponse(responseCode = "200", description = "폴더 이동에 성공하였습니다.")
    @PatchMapping("/{dirId}")
    public ResponseEntity<ResultResponse> moveDir(
        @PathVariable String dirId,
        @RequestParam String toDirId
    );

    @Operation(summary = "폴더 삭제 요청", description = "PathVariable로 dirId(삭제 대상 폴더 Id)를 받아 삭제")
    @ApiResponse(responseCode = "204", description = "폴더 삭제에 성공하였습니다.")
    @DeleteMapping("/{dirId}")
    public ResponseEntity<ResultResponse> deleteDir(@PathVariable String dirId);

    @Operation(summary = "폴더명 수정 요청", description = "PathVariable로 dirId(이름 수정 대상 폴더 Id), RequestBody로 newName을 받아 이름을 수정")
    @ApiResponse(responseCode = "200", description = "폴더 이름 수정에 성공하였습니다.")
    @PatchMapping("/{dirId}/rename")
    public ResponseEntity<ResultResponse> renameDir(
        @PathVariable String dirId,
        @RequestParam String newName
    );

    // 사진 조회
    @Operation(summary = "특정 폴더 내 사진파일 상세 조회", description = "PathVariable로 dirId와 fileId를 받아 해당 사진 정보를 반환")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "0", description = "응답 양식",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResultResponse.class)
            )
        ),
        @ApiResponse(responseCode = "200", description = "data 내용",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetDetailFileDto.class)
            ))
    })
    @GetMapping("/{dirId}/files/{fileId}")
    ResponseEntity<ResultResponse> getFile(String dirId, String fileId);

    // 사진 복사
    @Operation(summary = "(테스트 용도)단일 사진 복사 요청", description = "PathVariable로 fileId와 dirId(fromDirId), RequestParam으로 toDirId를 받아 사진을 복제하고 현재 폴더의 정보를 반환")
    @ApiResponse(responseCode = "200", description = "사진 복사에 성공하였습니다.")
    @PatchMapping("/{dirId}/files/{fileId}/copy")
    ResponseEntity<ResultResponse> copyFile(String fileId, String fromDirId, String toDirId);

    @Operation(summary = "(실 서비스 API)사진 복사 요청", description = "Body로 fileIdList와 dirId(fromDirId), RequestParam으로 toDirId를 받아 사진을 복제합니다")
    @ApiResponse(responseCode = "200", description = "사진 복사에 성공하였습니다.")
    @PatchMapping("/{dirId}/files/copy")
    public ResponseEntity<ResultResponse> copyFileList(
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId,
        @RequestBody FileListDto fileListDto
    );

    // 사진 이동
    @Operation(summary = "(테스트 용도)단일 사진 이동 요청", description = "PathVariable로 fileId와 dirId(fromDirId), RequestParam으로 toDirId를 받아 사진을 이동합니다.")
    @ApiResponse(responseCode = "200", description = "사진 이동에 성공하였습니다.")
    @PatchMapping("/{dirId}/files/{fileId}/move")
    ResponseEntity<ResultResponse> moveFile(String fileId, String fromDirId, String toDirId);

    @Operation(summary = "(실 서비스 API)복수 사진 이동 요청", description = "Body로 fileIdList, RequestParam으로 dirId(fromDirId), RequestParam으로 toDirId를 받아 사진을 이동합니다.")
    @ApiResponse(responseCode = "200", description = "사진 이동에 성공하였습니다.")
    @PatchMapping("/{dirId}/files/move")
    ResponseEntity<ResultResponse> moveFileList(
        @PathVariable(name = "dirId") String fromDirId,
        @RequestParam(name = "toDirId") String toDirId,
        @RequestBody FileListDto fileListDto
    );

    // 단일 사진 삭제
    @Operation(summary = "(테스트 용도)단일 사진 삭제 요청", description = "PathVariable로 dirId와 fileId를 받아 사진을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "사진 삭제에 성공하였습니다.")
    @DeleteMapping("/{dirId}/files/{fileId}")
    ResponseEntity<ResultResponse> deleteOneFile(String fileId, String dirId);

    @Operation(summary = "(실 서비스 API)복수 사진 삭제 요청", description = "Body로 fileIdList를 PathVarialbe로 dirId를 받아 사진을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "사진 삭제에 성공하였습니다.")
    @DeleteMapping("/{dirId}/files")
    public ResponseEntity<ResultResponse> deleteFileList(
        @PathVariable(name = "dirId") String dirId,
        @RequestBody FileListDto fileListDto
    );

    @Operation(summary = "특정 폴더의 하위 폴더 조회요청", description = "PathVariable로 dirId(부모 폴더 Id)를 받아 하위 폴더의 pk와 name을 반환")

    @ApiResponses(value = {
        @ApiResponse(responseCode = "0", description = "하위 폴더 조회에 성공하였습니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResultResponse.class)
            )
        ),
        @ApiResponse(responseCode = "200", description = "data 내용 아래의 DTO가 List에 쌓여옵니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetChildDirDto.class)
            ))
    })
    @GetMapping("/{dirId}/child")
    ResponseEntity<ResultResponse> getChildDir(String dirId);

    @Operation(summary = "사진 상세 조회시 thumnailurl2 조회 요청", description = "PathVariable로 dirId를 해당 폴더의 모든 사진을 반환")

    @ApiResponses(value = {
        @ApiResponse(responseCode = "0", description = "해당 폴더의 모든 썸네일2 사진 조회에 성공하였습니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResultResponse.class)
            )
        ),
        @ApiResponse(responseCode = "200", description = "data 내용 아래의 DTO가 list에 쌓여옵니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetDirThumbUrl2.class)
            ))
    })
    @GetMapping("/{dirId}/thumbnail2")
    ResponseEntity<ResultResponse> getDirThumbUrl2(String dirId);

}
