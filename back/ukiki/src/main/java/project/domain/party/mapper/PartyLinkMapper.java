package project.domain.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.party.dto.response.PartyEnterDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.entity.MemberParty;
import project.domain.party.redis.PartyLink;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartyLinkMapper {

    //PartyLink -> party -> id 를 뽑아서 partyEnterDto -> partyId 에 대입
    @Mapping(source = "party.id", target = "partyId")
    PartyEnterDto toPartyEnterDto(PartyLink partyLink);

    PartyLinkDto toPartyLinkDto(PartyLink partyLink);
}
