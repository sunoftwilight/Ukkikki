package project.domain.directory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import project.domain.article.dto.response.SimpleArticlePhotoDto;
import project.domain.directory.collection.File;
import project.domain.directory.dto.response.GetFileDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GetFileMapper {

    @Mappings({
        @Mapping(source = "file.id", target = "id"),
        @Mapping(source = "file.photoDto", target = "photo")
    })
    GetFileDto toGetFileDto(File file);


    @Mapping(source = "id", target = "fileId")
    @Mapping(source = "photoDto.id", target = "photoId")
    @Mapping(source = "photoDto.photoUrl", target = "photoUrl")
    @Mapping(target = "id", ignore = true) // 매핑 제외
    SimpleArticlePhotoDto toSimpleArticlePhotoDto(File file);
}
