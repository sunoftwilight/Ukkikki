package project.domain.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.member.dto.response.SimpleProfileDto;
import project.domain.member.entity.Profile;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {

    @Mapping(source = "member.id", target = "memberId")
    @Mapping(source = "party.id", target = "partyId")
    SimpleProfileDto simpleProfileDto(Profile profile);


    default List<SimpleProfileDto> toSimpleProfileDtoList(List<Profile> profileList){
        return profileList.stream()
            .map(this::simpleProfileDto)
            .collect(Collectors.toList());
    }
}
