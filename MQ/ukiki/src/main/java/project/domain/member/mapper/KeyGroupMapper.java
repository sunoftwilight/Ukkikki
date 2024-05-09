package project.domain.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.member.dto.response.KeyGroupDto;
import project.domain.member.entity.KeyGroup;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeyGroupMapper {

    @Mapping(source = "party.id", target = "partyId")
    @Mapping(source = "keyGroup.sseKey", target = "sseKey")
    KeyGroupDto toKeyGroupDto(KeyGroup keyGroup);

    default List<KeyGroupDto> toKeyGroupDtoList(List<KeyGroup> keyGroupList){
        return keyGroupList.stream()
                .map(this::toKeyGroupDto)
                .collect(Collectors.toList());
    }

}
