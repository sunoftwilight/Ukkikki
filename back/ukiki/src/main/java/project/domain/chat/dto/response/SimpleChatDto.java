package project.domain.chat.dto.response;

import lombok.Getter;
import lombok.Setter;
import project.domain.member.entity.ProfileType;

import java.time.LocalDateTime;

@Setter
@Getter
public class SimpleChatDto {

    String memberName;
    String content;
    LocalDateTime createDate;
    ProfileType profileType;
    String profileUrl;
    int readNum;
}
