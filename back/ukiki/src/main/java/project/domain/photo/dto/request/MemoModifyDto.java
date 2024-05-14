package project.domain.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemoModifyDto {
    Long memoId;
    String content;
}
