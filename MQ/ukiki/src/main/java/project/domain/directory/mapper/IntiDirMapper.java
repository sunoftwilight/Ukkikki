package project.domain.directory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.Directory;
import project.domain.directory.dto.response.InitDirDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IntiDirMapper {

    InitDirDto toInitDirDto(Directory directory);
}
