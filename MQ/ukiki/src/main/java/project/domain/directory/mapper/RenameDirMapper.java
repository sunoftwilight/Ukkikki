package project.domain.directory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.Directory;
import project.domain.directory.dto.response.InitDirDto;
import project.domain.directory.dto.response.RenameDirDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RenameDirMapper {
    RenameDirDto toRenameDirDto(Directory directory);
}
