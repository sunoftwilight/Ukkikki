package project.domain.article.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class ArticleUpdateDto {

    String title;
    String content;

    List<Object> articlePhotoList;
    boolean addPhoto;
}
