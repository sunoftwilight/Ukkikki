package project.domain.photo.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoDto {
    private Long photoId;
    private String content;
}
