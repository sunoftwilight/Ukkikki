package project.domain.article.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class ArticlePageDto {


    List<SimpleArticleDto> articleDtoList;

    int page;
    int size;
    boolean next;
}
