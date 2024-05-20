package project.domain.photo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.article.dto.response.SimpleArticlePhotoDto;
import project.domain.photo.entity.Photo;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    @Mapping(source = "id", target = "photoId")
    @Mapping(source = "photoUrl.photoUrl", target = "photoUrl")
    SimpleArticlePhotoDto toSimpleArticlePhotoDto(Photo photo);
}
