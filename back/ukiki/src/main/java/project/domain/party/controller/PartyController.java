package project.domain.party.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import project.domain.member.entity.MemberRole;
import project.domain.party.dto.request.EnterPartyDto;
import project.domain.party.dto.request.CreatePartyDto;
import project.domain.party.dto.request.PartyPasswordDto;
import project.domain.party.dto.response.PartyEnterDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.service.PartyService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;


@RequestMapping("/party")
@RestController
@RequiredArgsConstructor
public class PartyController implements PartyDocs {

    private final PartyService partyService;

    @Override
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> createParty(@RequestPart @Valid CreatePartyDto createPartyDto,
                                                      @RequestPart(required = false) MultipartFile photo) {
        PartyLinkDto response = partyService.createParty(createPartyDto, photo);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_PARTY_SUCCESS, response));
    }

    @Override
    @GetMapping("/link/{partyId}")
    public ResponseEntity<ResultResponse> makePartyLink(@PathVariable Long partyId) {
        PartyLinkDto response = partyService.createLink(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_PARTY_LINK_SUCCESS, response));
    }
    
    @Override
    @GetMapping("/enter/{partyLink}")  //파티 링크 유효 여부를 확인
    public ResponseEntity<ResultResponse> enterParty(@PathVariable(name = "partyLink") String partyLink) {
        partyService.enterParty(partyLink);
        return ResponseEntity.ok(new ResultResponse(ResultCode.PARTY_LINK_VALID));
    }
    
    @Override
    @PostMapping("/check/password")  // 그룹 비밀번호를 대조 확인
    public ResponseEntity<ResultResponse> checkPartyKey(@RequestBody EnterPartyDto enterPartyDto) {
        partyService.checkPassword(enterPartyDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.PASSWORD_CORRECT));
    }

    @Override
    @PostMapping("/enter/member")  // 멤버로 로그인
    public ResponseEntity<ResultResponse> memberPartyEnter(@RequestBody EnterPartyDto enterPartyDto) {
        PartyEnterDto partyEnterDto = partyService.memberPartyEnter(enterPartyDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MEMBER_ENTER_SUCCESS, partyEnterDto));
    }

    @Override
    @GetMapping("/enter/guest")   // 게스트 로그인
    public ResponseEntity<ResultResponse> guestPartyEnter(@RequestBody EnterPartyDto enterPartyDto) {
        PartyEnterDto partyEnterDto = partyService.guestPartyEnter(enterPartyDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GUEST_ENTER_SUCCESS, partyEnterDto));
    }

    @Override
    @PatchMapping("/change/password/{partyId}")
    public ResponseEntity<ResultResponse> changePartyPassword(@PathVariable Long partyId, @RequestBody PartyPasswordDto partyPasswordDto) {
        partyService.changePassword(partyId, partyPasswordDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CHANGE_PASSWORD_SUCCESS));
    }

    @Override
    @PatchMapping("/change/name/{partyId}")
    public ResponseEntity<ResultResponse> changePartyName(@PathVariable Long partyId, @RequestParam(name = "partyName") String partyName) {
        partyService.changePartyName(partyId, partyName);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CHANGE_PARTY_NAME_SUCCESS));
    }

    @Override
    @PatchMapping("/grant/{partyId}/{opponentId}")
    public ResponseEntity<ResultResponse> grantAuthority(@PathVariable Long partyId, @PathVariable Long opponentId, @RequestParam(name = "memberRole") MemberRole memberRole) {
        partyService.grantPartyUser(partyId, opponentId, memberRole);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GRANT_TARGET_SUCCESS));
    }

    @Override
    @DeleteMapping("/exit/{partyId}")
    public ResponseEntity<ResultResponse> exitParty(@PathVariable Long partyId, @RequestParam(name = "key")String key){
        partyService.exitParty(partyId, key);
        return null;
    }

}
