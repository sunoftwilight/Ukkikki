package project.domain.member.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {
    private Long id;
    private String userName;
    private String providerId;
}
