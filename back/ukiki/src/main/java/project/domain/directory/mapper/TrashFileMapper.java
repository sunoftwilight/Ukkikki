package project.domain.directory.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.File;
import project.domain.directory.dto.TrashFileDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrashFileMapper {

    @Mappings({
        @Mapping(source = "file.id", target = "id"),
        @Mapping(source = "file.photo", target = "photo"),
        @Mapping(target = "dirId", ignore = true),
    })
    TrashFileDto toTrashFile(File file, String dirId);

    @AfterMapping
    default void fillTrashFileDto(@MappingTarget TrashFileDto trashFileDto, String dirId) {
        trashFileDto.setDirId(dirId);
    }
}
