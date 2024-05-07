package project.domain.member.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoDto {
    Long userId;
    String userName;
    String profileUrl;
}
