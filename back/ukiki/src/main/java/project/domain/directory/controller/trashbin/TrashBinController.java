package project.domain.directory.controller.trashbin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import project.domain.directory.dto.request.TrashIdListDto;
import project.domain.directory.dto.response.GetTrashDto;
import project.global.result.ResultResponse;

@Tag(name ="휴지통 관련 Controller", description = "휴지통 조회, 삭제, 복구 API")
public interface TrashBinController {
    // 조회
    @Operation(summary = "쓰레기통 조회 요청", description = "PathVariable로 trashBinId를 받아 해당 쓰레기통 정보를 반환")

    @ApiResponses(value = {
        @ApiResponse(responseCode = "0", description = "쓰레기통 조회에 성공하였습니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResultResponse.class)
            )
        ),
        @ApiResponse(responseCode = "200", description = "data 내용 아래의 DTO가 리스트에 쌓여옵니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GetTrashDto.class)
            ))
    })
    @GetMapping("/{trashBinId}")
    ResponseEntity<ResultResponse> getTrashBin(Long trashBinId);

    // 쓰래기 복원
    @Operation(summary = "(테스트용)폴더 및 파일 복원 요청", description = "PathVariable로 trashBinId와 trashId를 받아 해당 쓰레기복원")
    @ApiResponse(responseCode = "201", description = "폴더 또는 파일복원에 성공하였습니다.")
    @PatchMapping("/{trashBinId}/trashes/{trashId}")
    ResponseEntity<ResultResponse> restoreOneTrash(Long trashBinId, String trashId);

    @Operation(summary = "(실 서비스 용)다중 폴더 및 파일 복원 요청", description = "PathVariable로 trashBinId, Body로 trashIdList를 받아 해당 쓰레기복원")
    @ApiResponse(responseCode = "201", description = "폴더 또는 파일복원에 성공하였습니다.")
    @PatchMapping("/{trashBinId}/trashes")
    ResponseEntity<ResultResponse> restoreTrashList(Long trashBinId, TrashIdListDto trashIdListDto);

    @Operation(summary = "(테스트용)단일 폴더 및 파일 삭제 요청", description = "PathVariable로 trashBinId과 trashId를 받아 해당 완전삭제")
    @ApiResponse(responseCode = "204", description = "쓰레기 삭제에 성공하였습니다.")
    @DeleteMapping("/{trashBinId}/trashes/{trashId}")
    ResponseEntity<ResultResponse> deleteOneTrash(Long trashBinId, String trashId);


    // 휴지통 비우기
    @Operation(summary = "(실 서비스 용)다중 폴더 및 파일 삭제 요청", description = "PathVariable로 trashBinId, Body로 trashIdList를 받아 해당 쓰레기복원")
    @ApiResponse(responseCode = "204", description = "쓰레기 삭제에 성공하였습니다.")
    @PatchMapping("/{trashBinId}/trashes")
    ResponseEntity<ResultResponse> deleteTrashList(Long trashBinId, TrashIdListDto trashIdListDto);

}
