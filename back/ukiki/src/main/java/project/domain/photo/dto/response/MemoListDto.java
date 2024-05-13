package project.domain.photo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoListDto {
    Long memoId;
    Long userId;
    String username;
    String content;

    public MemoListDto(Long id, String content, Long userId, String userName) {
        this.memoId = id;
        this.content = content;
        this.userId = userId;
        this.username = userName;
    }
}
