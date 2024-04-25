package project.domain.party.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import project.domain.party.dto.request.EnterPartyDto;
import project.domain.party.dto.request.CreatePartyDto;
import project.domain.party.dto.request.PartyPasswordDto;
import project.global.result.ResultResponse;



@Tag(name = "GroupController", description = "그룹 api")
public interface PartyDocs {


    @Operation(summary = "그룹 생성")
    ResponseEntity<ResultResponse> createParty(CreatePartyDto createPartyDto, MultipartFile photo);

    @Operation(summary = "그룹 링크 생성")
    ResponseEntity<ResultResponse> makePartyLink(Long partyId);

    @Operation(summary = "그룹 참여 링크")
    ResponseEntity<ResultResponse> enterParty(String partyLink);

    @Operation(summary = "파티 암호키 체크")
    ResponseEntity<ResultResponse> checkPartyKey(EnterPartyDto checkPartyEnterDto);

    @Operation(summary = "회원 파티 참가하기")
    ResponseEntity<ResultResponse> memberPartyEnter(EnterPartyDto checkPartyEnterDto);

    @Operation(summary = "게스트 파티 참가하기")
    ResponseEntity<ResultResponse> guestPartyEnter(EnterPartyDto enterPartyDto);

    @Operation(summary = "파티 암호 바꾸기")
    ResponseEntity<ResultResponse> changePartyPassword(Long partyId, PartyPasswordDto partyPasswordDto);

    @Operation(summary = "파티 이름 바꾸기")
    @Parameter(name = "partyName", description = "바꿀 파티 이름")
    ResponseEntity<ResultResponse> changePartyName(Long partyId, String partyName);
}
