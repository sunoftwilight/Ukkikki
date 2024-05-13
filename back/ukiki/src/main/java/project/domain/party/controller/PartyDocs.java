package project.domain.party.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.domain.member.entity.MemberRole;
import project.domain.party.dto.request.*;
import project.global.result.ResultResponse;

import java.io.IOException;


@Tag(name = "GroupController", description = "그룹 api")
public interface PartyDocs {


    @Operation(summary = "그룹 생성")
    ResponseEntity<ResultResponse> createParty(CreatePartyDto createPartyDto, MultipartFile photo);

    @Operation(summary = "그룹 링크 생성")
    ResponseEntity<ResultResponse> makePartyLink(Long partyId);

    @Operation(summary = "그룹 참여 링크")
    void enterParty(RedirectAttributes redirect, HttpServletResponse response, String partyLink) throws IOException;

    @Operation(summary = "파티 암호키 체크")
    ResponseEntity<ResultResponse> checkChangedPartyKey(CheckChangePasswordDto checkChangePasswordDto);

    @Operation(summary = "파티 암호키 체크")
    ResponseEntity<ResultResponse> checkPartyKey(Long partyId, EnterPartyDto checkPartyEnterDto);

    @Operation(summary = "회원 파티 참가하기")
    ResponseEntity<ResultResponse> memberPartyEnter(Long partyId);

    @Operation(summary = "게스트 파티 참가하기")
    ResponseEntity<ResultResponse> guestPartyEnter(Long partyId);

    @Operation(summary = "파티 암호 바꾸기")
    ResponseEntity<ResultResponse> changePartyPassword(Long partyId, PartyPasswordDto partyPasswordDto);


    @Operation(summary = "파티 권한 주기")
    ResponseEntity<ResultResponse> grantAuthority(Long partyId, Long opponentId, MemberRole memberRole);

    @Operation(summary = "파티 나가기")
    ResponseEntity<ResultResponse> exitParty(Long partyId, String key);

    @Operation(summary = "파티원 차단하기")
    ResponseEntity<ResultResponse> memberBlock(Long partyId, Long targetId);

    @Operation(summary = "파티원 추방하기")
    ResponseEntity<ResultResponse> kickMember(Long partyId, Long targetId);

    @Operation(summary = "차단 유저 목록 조회")
    ResponseEntity<ResultResponse> getBlockUserList(Long partyId);

    @Operation(summary = "유저 목록 조회")
    ResponseEntity<ResultResponse> getUserList(Long partyId);

    @Operation(summary = "파티 썸네일 변경")
    ResponseEntity<ResultResponse> changePartyThumb(Long partyId, ChangeThumbDto changeThumbDto, MultipartFile photo);
    @Operation(summary = "파티 이름 바꾸기")
    @Parameter(name = "partyName", description = "바꿀 파티 이름")
    ResponseEntity<ResultResponse> changePartyName(Long partyId, String partyName);

    @Operation(summary = "파티 이름 썸네일 바꾸기")
    ResponseEntity<ResultResponse> changePartyInfo(Long partyId, ChangeThumbDto changeThumbDto, MultipartFile photo);

    @Operation(summary = "파티 리스트 조회")
    ResponseEntity<ResultResponse> getPartyList();

    @Operation(summary = "파티 디테일 조회")
    ResponseEntity<ResultResponse> getPartyDetail(Long partyId);

    @Operation(summary = "파티 프로필 변경")
    @Parameter(name = "nickName", description = "바꿀 프로필 이름")
    ResponseEntity<Object> chagneProfile(Long partyId, ChangeProfileDto profileDto, MultipartFile photo);
}
