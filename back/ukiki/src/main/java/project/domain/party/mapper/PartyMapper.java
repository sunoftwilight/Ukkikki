package project.domain.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.domain.party.dto.response.PartyDto;

import project.domain.party.entity.Party;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartyMapper {

    PartyDto toPartyDto(Party party);
}
