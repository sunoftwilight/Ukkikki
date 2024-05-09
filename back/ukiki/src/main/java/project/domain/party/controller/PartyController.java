package project.domain.party.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import project.domain.member.entity.MemberRole;
import project.domain.party.dto.request.*;
import project.domain.party.dto.response.CheckPasswordDto;
import project.domain.party.dto.response.PartyEnterDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.dto.response.SimpleMemberPartyDto;
import project.domain.party.service.PartyService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.util.List;


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
    @PostMapping("/check/changed-password")  // 그룹 비밀번호 변경시 기존유저 대조 확인
    public ResponseEntity<ResultResponse> checkChangedPartyKey(@RequestBody CheckChangePasswordDto checkChangePasswordDto) {
        CheckPasswordDto response = partyService.checkChangedPassword(checkChangePasswordDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.PASSWORD_CORRECT, response));
    }

    @Override
    @PostMapping("/check/password")  // 그룹 비밀번호를 대조 확인
    public ResponseEntity<ResultResponse> checkPartyKey(@RequestBody EnterPartyDto enterPartyDto) {
        CheckPasswordDto response = partyService.checkPassword(enterPartyDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.PASSWORD_CORRECT, response));
    }

    @Override
    @PostMapping("/enter/member")  // 멤버로 로그인
    public ResponseEntity<ResultResponse> memberPartyEnter(@RequestBody EnterPartyDto enterPartyDto) {
        PartyEnterDto response = partyService.memberPartyEnter(enterPartyDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MEMBER_ENTER_SUCCESS, response));
    }

    @Override
    @GetMapping("/enter/guest")   // 게스트 로그인
    public ResponseEntity<ResultResponse> guestPartyEnter(@RequestBody EnterPartyDto enterPartyDto) {
        PartyEnterDto response = partyService.guestPartyEnter(enterPartyDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GUEST_ENTER_SUCCESS, response));
    }

    @Override   // 비밀번호 변경
    @PatchMapping("/change/password/{partyId}")
    public ResponseEntity<ResultResponse> changePartyPassword(@PathVariable Long partyId, @RequestBody PartyPasswordDto partyPasswordDto) {
        CheckPasswordDto response = partyService.changePassword(partyId, partyPasswordDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CHANGE_PASSWORD_SUCCESS, response));
    }

    @Override   // 파티명 변경
    @PatchMapping("/change/name/{partyId}")
    public ResponseEntity<ResultResponse> changePartyName(@PathVariable Long partyId, @RequestParam(name = "partyName") String partyName) {
        partyService.changePartyName(partyId, partyName);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CHANGE_PARTY_NAME_SUCCESS));
    }

    @Override   // 파티원 권한 부여
    @PatchMapping("/grant/{partyId}/{opponentId}")
    public ResponseEntity<ResultResponse> grantAuthority(@PathVariable Long partyId, @PathVariable Long opponentId, @RequestParam(name = "memberRole") MemberRole memberRole) {
        partyService.grantPartyUser(partyId, opponentId, memberRole);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GRANT_TARGET_SUCCESS));
    }

    @Override   // 파티 나가기
    @DeleteMapping("/exit/{partyId}")
    public ResponseEntity<ResultResponse> exitParty(@PathVariable Long partyId, @RequestParam(name = "key") String key) {
        partyService.exitParty(partyId, key);
        return null;
    }

    @Override
    @PatchMapping("/block/{partyId}/{targetId}")
    public ResponseEntity<ResultResponse> memberBlock(@PathVariable(name = "partyId") Long partyId, @PathVariable(name = "targetId") Long targetId) {
        partyService.memberBlock(partyId, targetId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MEMBER_BLOCK_SUCCESS));
    }

    @Override
    @DeleteMapping("/kick/{partyId}/{targetId}")
    public ResponseEntity<ResultResponse> kickMember(@PathVariable(name = "partyId") Long partyId, @PathVariable(name = "targetId") Long targetId) {
        partyService.kickMember(partyId, targetId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MEMBER_KICK_SUCCESS));
    }

    @Override
    @GetMapping("/block/user-list/{partyId}")
    public ResponseEntity<ResultResponse> getBlockUserList(@PathVariable(name = "partyId") Long partyId) {
        List<SimpleMemberPartyDto> response = partyService.getBlockUserList(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_BLOCK_USER_LIST_SUCCESS, response));
    }

    @Override
    @GetMapping("/user-list/{partyId}")
    public ResponseEntity<ResultResponse> getUserList(@PathVariable(name = "partyId") Long partyId) {
        List<SimpleMemberPartyDto> response = partyService.getUserList(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_USERLIST_SUCCESS, response));
    }

    @Override
    @PatchMapping(value = "/change/thumb/{partyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> changePartyThumb(@PathVariable(name = "partyId") Long partyId,
                                                           @RequestPart @Valid ChangeThumbDto changeThumbDto,
                                                           @RequestPart MultipartFile photo) {
        partyService.changePartyThumb(partyId, changeThumbDto, photo);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CHANGE_THUMB_SUCCESS));
    }

}
