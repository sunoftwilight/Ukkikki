package project.domain.photo.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoModifyDto {
    Long memoId;
    String content;
}
