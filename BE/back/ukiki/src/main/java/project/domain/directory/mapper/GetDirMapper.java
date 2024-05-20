package project.domain.directory.mapper;

import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.Directory;
import project.domain.directory.dto.response.GetDirDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GetDirMapper {
    @Mappings({
        @Mapping(source = "directory.id", target = "id"),
        @Mapping(source = "directory.dirName", target = "dirName"),
        @Mapping(source = "directory.parentDirId", target = "parentDirId"),
        @Mapping(target = "parentDirName", ignore = true),
        @Mapping(source = "directory.childDirIdList", target = "childDirIdList"),
        @Mapping(target  = "childDirNameList", ignore = true),
        @Mapping(source = "directory.fileIdList", target = "fileIdList"),
        @Mapping(target = "photoUrlList", ignore = true)
    })
    GetDirDto toGetDirDto(
        Directory directory,
        String parentDirName,
        List<String> childDirNameList,
        List<String> photoUrlList
    );

    @AfterMapping
    default void fillGetDir(
        @MappingTarget GetDirDto getDirDto,
        String parentDirName,
        List<String> childDirNameList,
        List<String> photoUrlList
        ) {
        getDirDto.setParentDirName(parentDirName);
        getDirDto.setChildDirNameList(childDirNameList);
        getDirDto.setPhotoUrlList(photoUrlList);
    }
}
