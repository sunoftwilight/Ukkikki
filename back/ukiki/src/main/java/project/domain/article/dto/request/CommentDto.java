package project.domain.article.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentDto {
    String content;
    List<Tag> tagList;

    @Getter
    @Setter
    public static class Tag{
        Long userId;
        String userName;
    }
}
