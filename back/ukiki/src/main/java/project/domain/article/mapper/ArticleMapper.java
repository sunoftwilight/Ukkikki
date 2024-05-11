package project.domain.article.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.article.dto.response.SimpleArticleDto;
import project.domain.article.entity.Article;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    @Mapping(source = "party.id", target = "partyId")
    SimpleArticleDto toSimpleArticleDto(Article article);
}
