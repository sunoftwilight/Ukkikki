package project.domain.article.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.article.dto.response.SimpleArticlePhotoDto;
import project.domain.article.entity.ArticlePhoto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticlePhotoMapper {


    @Mapping(source = "photo.photoUrl.photoUrl", target = "photoUrl")
    @Mapping(source = "photo.id", target = "photoId")
    SimpleArticlePhotoDto toSimpleArticlePhotoDto(ArticlePhoto articlePhoto);

    default List<SimpleArticlePhotoDto> toSimpleArticlePhotoDtoList(List<ArticlePhoto> articlePhotoList){
        return articlePhotoList.stream()
            .map(this::toSimpleArticlePhotoDto)
            .collect(Collectors.toList());
    }
}
