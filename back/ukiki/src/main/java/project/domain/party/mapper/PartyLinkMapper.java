package project.domain.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.redis.PartyLink;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartyLinkMapper {



    PartyLinkDto toPartyLinkDto(PartyLink partyLink);
}
