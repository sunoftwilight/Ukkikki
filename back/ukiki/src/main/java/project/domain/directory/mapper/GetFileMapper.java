package project.domain.directory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.File;
import project.domain.directory.dto.response.GetFileDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GetFileMapper {

    @Mappings({
        @Mapping(source = "file.id", target = "id"),
        @Mapping(source = "file.photoDto", target = "photo")
    })
    GetFileDto toGetFileDto(File file);
}
