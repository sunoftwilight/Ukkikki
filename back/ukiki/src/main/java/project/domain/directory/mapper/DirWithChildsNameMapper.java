package project.domain.directory.mapper;

import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.Directory;
import project.domain.directory.dto.response.DirWithChildsNameDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DirWithChildsNameMapper {

    @Mappings({
        @Mapping(source = "directory.id", target = "id"),
        @Mapping(source = "directory.dirName", target = "dirName"),
        @Mapping(source = "directory.parentDirId", target = "parentDirId"),
        @Mapping(source = "directory.childDirIdList", target = "childDirIdList"),
        @Mapping(target  = "childDirNameList", ignore = true),
        @Mapping(source = "directory.fileIdList", target = "fileIdList")
    })
    DirWithChildsNameDto toDirWithChildsNameDto(Directory directory, List<String> childDirNameList);

    @AfterMapping
    default void fillChildDirNames(@MappingTarget DirWithChildsNameDto dto, List<String> childDirNameList) {
        dto.setChildDirNameList(childDirNameList);
    }
}
