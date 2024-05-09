package project.domain.article.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleCreateDto {

    String title;
    String content;
    List<Long> photoIdList;
}
