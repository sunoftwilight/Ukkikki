package project.domain.party.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import project.domain.member.entity.MemberRole;
import project.domain.party.dto.request.ChangeThumbDto;
import project.domain.party.dto.request.EnterPartyDto;
import project.domain.party.dto.request.CreatePartyDto;
import project.domain.party.dto.request.PartyPasswordDto;
import project.global.result.ResultResponse;



@Tag(name = "GroupController", description = "그룹 api")
public interface PartyDocs {


    @Operation(summary = "그룹 생성")
    ResponseEntity<ResultResponse> createParty(UserDetails userDetails, CreatePartyDto createPartyDto, MultipartFile photo);

    @Operation(summary = "그룹 링크 생성")
    ResponseEntity<ResultResponse> makePartyLink(UserDetails userDetails, Long partyId);

    @Operation(summary = "그룹 참여 링크")
    ResponseEntity<ResultResponse> enterParty(String partyLink);

    @Operation(summary = "파티 암호키 체크")
    ResponseEntity<ResultResponse> checkPartyKey(EnterPartyDto checkPartyEnterDto);

    @Operation(summary = "회원 파티 참가하기")
    ResponseEntity<ResultResponse> memberPartyEnter(UserDetails userDetails, EnterPartyDto checkPartyEnterDto);

    @Operation(summary = "게스트 파티 참가하기")
    ResponseEntity<ResultResponse> guestPartyEnter(EnterPartyDto enterPartyDto);

    @Operation(summary = "파티 암호 바꾸기")
    ResponseEntity<ResultResponse> changePartyPassword(UserDetails userDetails, Long partyId, PartyPasswordDto partyPasswordDto);

    @Operation(summary = "파티 이름 바꾸기")
    @Parameter(name = "partyName", description = "바꿀 파티 이름")
    ResponseEntity<ResultResponse> changePartyName(UserDetails userDetails, Long partyId, String partyName);

    @Operation(summary = "파티 권한 주기")
    ResponseEntity<ResultResponse> grantAuthority(UserDetails userDetails, Long partyId, Long opponentId, MemberRole memberRole);

    @Operation(summary = "파티 나가기")
    ResponseEntity<ResultResponse> exitParty(UserDetails userDetails, Long partyId, String key);

    @Operation(summary = "파티원 차단하기")
    ResponseEntity<ResultResponse> memberBlock(UserDetails userDetails, Long partyId, Long targetId);
    
    @Operation(summary = "파티원 추방하기")
    ResponseEntity<ResultResponse> kickMember(UserDetails userDetails, Long partyId, Long targetId);

    @Operation(summary = "차단 유저 목록 조회")
    ResponseEntity<ResultResponse> getBlockUserList(UserDetails userDetails, Long partyId);

    @Operation(summary = "유저 목록 조회")
    ResponseEntity<ResultResponse> getUserList(UserDetails userDetails, Long partyId);

    @Operation(summary = "파티 썸네일 변경")
    ResponseEntity<ResultResponse> changePartyThumb(UserDetails userDetails, Long partyId, ChangeThumbDto changeThumbDto, MultipartFile photo);
}
