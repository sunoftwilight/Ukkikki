package project.domain.photo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.directory.dto.response.GetDirInnerDtov2;
import project.domain.photo.dto.request.GroupDetailReqDto;
import project.domain.photo.dto.response.GroupDetailResDto;
import project.domain.photo.dto.response.GroupbrieflyDto;
import project.domain.photo.service.PhotoGroupService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoGroupController implements PhotoGroupDocs {

    private final PhotoGroupService photoGroupService;

    @Override
    @GetMapping("/group/{partyId}")
    public ResponseEntity<ResultResponse> photoGroup(@PathVariable Long partyId){
        List<GroupbrieflyDto> groupbrieflyDtoList = photoGroupService.getGroups(partyId);
        return ResponseEntity.ok().body(new ResultResponse(ResultCode.GET_FILE_SUCCESS, groupbrieflyDtoList));
    }

    @Override
    @GetMapping("/group/detail/{partyId}")
    public ResponseEntity<ResultResponse> photoGroupDetail(GroupDetailReqDto groupDetailReqDto, @PathVariable Long partyId){
        List<GetDirInnerDtov2> groupDetailReqDtoList = photoGroupService.getGroupDetail(groupDetailReqDto.getType(), groupDetailReqDto.getGroupName(), partyId);
        return ResponseEntity.ok().body(new ResultResponse(ResultCode.GET_FILE_SUCCESS, groupDetailReqDtoList));
    }

}
