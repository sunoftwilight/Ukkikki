package project.domain.member.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class InfoDto {
    Long userId;
    String userName;
    String profileUrl;
    boolean isInsertPass;
    Long uploadGroupId;
}
