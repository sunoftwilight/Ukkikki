package project.domain.chat.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.domain.chat.entity.ChatType;

@Getter
@Setter
@ToString
public class ChatDto {

    private String content;
    private String password;
}
