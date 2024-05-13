package project.domain.party.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import project.domain.member.entity.MemberRole;
import project.domain.member.entity.Profile;
import project.domain.party.dto.request.*;
import project.domain.party.dto.response.*;
import project.domain.party.redis.PartyLink;

import java.util.List;

public interface PartyService {

    PartyLinkDto createParty(CreatePartyDto createPartyDto, MultipartFile photo);

    List<SimplePartyDto> getPartyList();

    PartyDto getPartyDetail(Long partyId);

    PartyLinkDto createLink(Long partyId);

    PartyLink enterParty(String partyLink);

    CheckPasswordDto checkChangedPassword(CheckChangePasswordDto checkChangePasswordDto);

    CheckPasswordDto checkPassword(EnterPartyDto enterPartyDto);

    PartyEnterDto memberPartyEnter(EnterPartyDto enterPartyDto);

    PartyEnterDto guestPartyEnter(EnterPartyDto enterPartyDto);

    CheckPasswordDto changePassword(Long partyId, PartyPasswordDto partyPasswordDto);

    void changePartyName(Long partyId, String partyName);

    void grantPartyUser(Long partyId, Long opponentId, MemberRole memberRole);

    void exitParty(Long partyId, String key);

    void memberBlock(Long partyId, Long targetId);

    void kickMember(Long partyId, Long targetId);

    List<SimpleMemberPartyDto> getBlockUserList(Long partyId);
    List<SimpleMemberPartyDto> getUserList(Long partyId);

    void changePartyThumb(Long partyId, ChangeThumbDto changeThumbDto, MultipartFile photo);

    void linkDelete();

    Profile partyProfileChange(Long partyId, ChangeProfileDto profileDto, MultipartFile photo);
}
