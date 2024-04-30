package project.domain.party.service;


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

    PartyLinkDto createParty(CreatePartyDto createPartyDto, MultipartFile photo);

    PartyLinkDto createLink(Long partyId);

    void enterParty(String partyLink);

    void checkPassword(EnterPartyDto enterPartyDto);

    PartyEnterDto memberPartyEnter(EnterPartyDto enterPartyDto);

    PartyEnterDto guestPartyEnter(EnterPartyDto enterPartyDto);

    void changePassword(Long partyId, PartyPasswordDto partyPasswordDto);

    void changePartyName(Long partyId, String partyName);

    void grantPartyUser(Long partyId, Long opponentId, MemberRole memberRole);

    void exitParty(Long partyId, String key);

    void memberBlock(Long partyId, Long targetId);

    void kickMember(Long partyId, Long targetId);

    List<SimpleMemberPartyDto> getBlockUserList(Long partyId);
    List<SimpleMemberPartyDto> getUserList(Long partyId);

    void changePartyThumb(Long partyId, ChangeThumbDto changeThumbDto, MultipartFile photo);

    void linkDelete();
}
