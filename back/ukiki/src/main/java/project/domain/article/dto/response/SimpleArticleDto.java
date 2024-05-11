package project.domain.article.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class SimpleArticleDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createDate;
    private boolean modify;
    private Long partyId;
    private List<SimpleArticlePhotoDto> photoList;
}
