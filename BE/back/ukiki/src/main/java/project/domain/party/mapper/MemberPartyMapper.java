package project.domain.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.party.dto.response.SimpleMemberPartyDto;
import project.domain.party.dto.response.SimplePartyDto;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberPartyMapper {

    @Mapping(source = "member.userName", target = "userName")
    @Mapping(source = "member.profileUrl", target = "profileUrl")
    @Mapping(source = "member.id", target = "userId")
    @Mapping(source = "party.id", target = "partyId")
    SimpleMemberPartyDto toSimplePartyMemberDto(MemberParty memberParty);

    default List<SimpleMemberPartyDto> toSimplePartyMemberDtoList(List<MemberParty> memberPartyList){
        return memberPartyList.stream()
            .map(this::toSimplePartyMemberDto)
            .collect(Collectors.toList());
    }

    @Mapping(source = "thumbnail", target = "partyProfile")
    SimplePartyDto toSimplePartyDto(Party party);

    default List<SimplePartyDto> toSimplePartyDtoList(List<Party> partyList){
        return partyList.stream()
            .map(this::toSimplePartyDto)
            .collect(Collectors.toList());
    }
}
