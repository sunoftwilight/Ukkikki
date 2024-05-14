package project.domain.article.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDirDto {

    Long id;
    Long partyId;
    String title;
    String writer;
}
