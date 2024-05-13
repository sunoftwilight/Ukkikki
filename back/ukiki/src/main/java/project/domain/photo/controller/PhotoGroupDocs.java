package project.domain.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.domain.photo.dto.request.GroupDetailReqDto;
import project.domain.photo.dto.response.GroupDetailResDto;
import project.domain.photo.dto.response.GroupbrieflyDto;
import project.global.result.ResultResponse;

import java.util.List;

public interface PhotoGroupDocs {

    @Operation(summary = "그룹 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "그룹 목록 조회 성공.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResultResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "200", description = "data 내용 아래의 DTO가 리스트에 쌓여옵니다. Type 에 관혀여 1 : GPT 에 의해 생성된 그룹, 2 : 인물분류, 3 : 좋아요" +
                    "groupName 에 관하여 type 1은 Enum 클래스로 만들어진 이름, type 2는 인물분류 그룹 번호, 3은 좋아요로 표시함",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupbrieflyDto.class)
                    ))
    })
    @GetMapping("/group/{partyId}")
    public ResponseEntity<ResultResponse> photoGroup(@PathVariable Long partyId);

    @Operation(summary = "그룹 디테일 조회", description = "type 1과 2는 이름 파라메터가 필수이고 3은 공백으로 보내면 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "그룹 디테일 조회 성공.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResultResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "200", description = "data 내용 아래의 DTO가 리스트에 쌓여옵니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupDetailResDto.class)
                    ))
    })
    @GetMapping("/group/detail/{partyId}")
    public ResponseEntity<ResultResponse> photoGroupDetail(GroupDetailReqDto groupDetailReqDto, @PathVariable Long partyId);

}
