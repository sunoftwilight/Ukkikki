package project.domain.article.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleArticlePhotoDto {

    Long id;
    Long photoId;
    String fileId;
    String photoUrl;



}
