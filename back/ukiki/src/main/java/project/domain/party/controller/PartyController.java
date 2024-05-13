package project.domain.party.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.domain.member.entity.MemberRole;
import project.domain.member.entity.Profile;
import project.domain.party.dto.request.*;
import project.domain.party.dto.response.*;
import project.domain.party.redis.PartyLink;
import project.domain.party.service.PartyService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/party")
@RestController
@RequiredArgsConstructor
public class PartyController implements PartyDocs {


    private final PartyService partyService;

    @Override
    @PostMapping(value = "/create", consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<ResultResponse> createParty(@RequestPart @Valid CreatePartyDto createPartyDto,
                                                      @RequestPart(required = false) MultipartFile photo) {
        PartyLinkDto response = partyService.createParty(createPartyDto, photo);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_PARTY_SUCCESS, response));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<ResultResponse> getPartyList() {
        List<SimplePartyDto> res = partyService.getPartyList();
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_PARTY_LIST_SUCCESS, res));
    }

    @Override
    @GetMapping("/{partyId}")
    public ResponseEntity<ResultResponse> getPartyDetail(@PathVariable(name = "partyId")Long partyId) {
        PartyDto res = partyService.getPartyDetail(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_PARTY_DETAIL_SUCCESS, res));
    }

    @Override
    @PostMapping(value = "/change-profile/{partyId}", consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<Object> chagneProfile(@PathVariable(name = "partyId") Long partyId, @RequestPart ChangeProfileDto profileDto, @RequestPart(required = false)MultipartFile photo) {
        Profile res = partyService.partyProfileChange(partyId, profileDto, photo);
        return ResponseEntity.ok(res);
    }

    @Override
    @GetMapping("/link/{partyId}")  // 링크 만들기
    public ResponseEntity<ResultResponse> makePartyLink(@PathVariable Long partyId) {
        PartyLinkDto response = partyService.createLink(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_PARTY_LINK_SUCCESS, response));
    }

    @Override
    @GetMapping("/enter/{partyLink}")  //파티 링크 유효 여부를 확인
    public void enterParty(RedirectAttributes redirect, HttpServletResponse response, @PathVariable(name = "partyLink") String partyLink) throws IOException {
        PartyLink link = partyService.enterParty(partyLink);

        redirect.addAttribute(link.getParty());
        response.sendRedirect(String.format("/group/%d/attend", link.getParty()));
//        response.sendRedirect(String.format("http://localhost:5173/group/%d/attend", link.getParty()));

    }

    @Override
    @PostMapping("/check/changed-password")  // 그룹 비밀번호 변경시 기존유저 대조 확인
    public ResponseEntity<ResultResponse> checkChangedPartyKey(@RequestBody CheckChangePasswordDto checkChangePasswordDto) {
        CheckPasswordDto response = partyService.checkChangedPassword(checkChangePasswordDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.PASSWORD_CORRECT, response));
    }

    @Override
    @PostMapping("/check/password/{partyId}")  // 그룹 비밀번호를 대조 확인
    public ResponseEntity<ResultResponse> checkPartyKey(@PathVariable(name = "partyId")Long partyId, @RequestBody EnterPartyDto enterPartyDto) {
        CheckPasswordDto response = partyService.checkPassword(partyId, enterPartyDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.PASSWORD_CORRECT, response));
    }

    @Override
    @GetMapping("/enter/member/{partyId}")  // 멤버로 로그인
    public ResponseEntity<ResultResponse> memberPartyEnter(@PathVariable(name = "partyId") Long partyId) {
        PartyEnterDto response = partyService.memberPartyEnter(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.MEMBER_ENTER_SUCCESS, response));
    }

    @Override
    @GetMapping("/enter/guest/{partyId}")   // 게스트 로그인
    public ResponseEntity<ResultResponse> guestPartyEnter(@PathVariable(name = "partyId") Long partyId) {
        PartyEnterDto response = partyService.guestPartyEnter(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GUEST_ENTER_SUCCESS, response));
    }

    @Override   // 비밀번호 변경
    @PatchMapping("/change/password/{partyId}")
    public ResponseEntity<ResultResponse> changePartyPassword(@PathVariable(name = "partyId") Long partyId, @RequestBody PartyPasswordDto partyPasswordDto) {
        CheckPasswordDto response = partyService.changePassword(partyId, partyPasswordDto);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CHANGE_PASSWORD_SUCCESS, response));
    }

    @Override   // 파티명 변경
    @PatchMapping("/change/name/{partyId}")
    public ResponseEntity<ResultResponse> changePartyName(@PathVariable(name = "partyId") Long partyId, @RequestParam(name = "partyName") String partyName) {
        partyService.changePartyName(partyId, partyName);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CHANGE_PARTY_NAME_SUCCESS));
    }

    @Override
    @PatchMapping("/change/party-info/{partyId}")
    public ResponseEntity<ResultResponse> changePartyInfo(@PathVariable(name = "partyId") Long partyId, @RequestPart ChangeThumbDto changeThumbDto, @RequestPart(required = false) MultipartFile photo) {
        partyService.changePartyInfo(partyId,changeThumbDto,photo);
        return null;
    }

    @Override   // 파티원 권한 부여    `
    @PatchMapping("/grant/{partyId}/{opponentId}")
    public ResponseEntity<ResultResponse> grantAuthority(@PathVariable(name = "partyId") Long partyId, @PathVariable Long opponentId, @RequestParam(name = "memberRole") MemberRole memberRole) {
        partyService.grantPartyUser(partyId, opponentId, memberRole);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GRANT_TARGET_SUCCESS));
    }

    @Override   // 파티 나가기
    @DeleteMapping("/exit/{partyId}")
    public ResponseEntity<ResultResponse> exitParty(@PathVariable(name = "partyId") Long partyId, @RequestParam(name = "key") String key) {
        partyService.exitParty(partyId, key);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GRANT_TARGET_SUCCESS));
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
