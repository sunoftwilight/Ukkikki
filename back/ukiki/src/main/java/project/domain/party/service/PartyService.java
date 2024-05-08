package project.domain.party.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import project.domain.member.entity.MemberRole;
import project.domain.party.dto.request.ChangeThumbDto;
import project.domain.party.dto.request.EnterPartyDto;
import project.domain.party.dto.request.CreatePartyDto;
import project.domain.party.dto.request.PartyPasswordDto;
import project.domain.party.dto.response.PartyEnterDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.dto.response.SimpleMemberPartyDto;

import java.util.List;

public interface PartyService {

    PartyLinkDto createParty(UserDetails userDetails, CreatePartyDto createPartyDto, MultipartFile photo);

    PartyLinkDto createLink(UserDetails userDetails, Long partyId);

    void enterParty(String partyLink);

    void checkPassword(EnterPartyDto enterPartyDto);

    PartyEnterDto memberPartyEnter(UserDetails userDetails, EnterPartyDto enterPartyDto);

    PartyEnterDto guestPartyEnter(EnterPartyDto enterPartyDto);

    void changePassword(UserDetails userDetails, Long partyId, PartyPasswordDto partyPasswordDto);

    void changePartyName(UserDetails userDetails, Long partyId, String partyName);

    void grantPartyUser(UserDetails userDetails, Long partyId, Long opponentId, MemberRole memberRole);

    void exitParty(UserDetails userDetails, Long partyId, String key);

    void memberBlock(UserDetails userDetails, Long partyId, Long targetId);

    void kickMember(UserDetails userDetails, Long partyId, Long targetId);

    List<SimpleMemberPartyDto> getBlockUserList(UserDetails userDetails, Long partyId);
    List<SimpleMemberPartyDto> getUserList(UserDetails userDetails, Long partyId);

    void changePartyThumb(UserDetails userDetails, Long partyId, ChangeThumbDto changeThumbDto, MultipartFile photo);

    void linkDelete();
}
