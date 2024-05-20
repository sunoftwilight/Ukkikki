package project.domain.member.dto.response;

import lombok.Getter;
import lombok.Setter;
import project.domain.member.entity.ProfileType;


@Getter
@Setter
public class SimpleProfileDto {


    private ProfileType type;
    private String nickname;
    private String profileUrl;
    private Long memberId;
    private Long partyId;

}
