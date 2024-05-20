package project.domain.article.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleCreateDto {

    String title;
    String content;
    String password;
    List<String> photoIdList;
}
