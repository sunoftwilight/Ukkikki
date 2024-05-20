package project.domain.directory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.Directory;
import project.domain.directory.dto.response.DirDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DirMapper {

    DirDto toDirDto(Directory directory);
}
