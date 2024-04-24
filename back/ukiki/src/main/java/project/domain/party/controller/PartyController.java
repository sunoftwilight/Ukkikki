package project.domain.party.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import project.domain.party.dto.request.CreateGroupDto;
import project.global.result.ResultResponse;



@Tag(name = "GroupController", description = "그룹 api")
public interface PartyController {


    @Operation(summary = "그룹 생성")
    public ResponseEntity<ResultResponse> createParty(CreateGroupDto createGroupDto,  MultipartFile photo);

    @Operation(summary = "그룹 링크 생성")
    public ResponseEntity<ResultResponse> makePartyLink(Long partyId);


}
