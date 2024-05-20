package project.domain.party.dto.response;

import lombok.Getter;
import lombok.Setter;
import project.domain.member.entity.MemberRole;

@Getter
@Setter
public class SimpleMemberPartyDto {

    Long partyId;
    Long userId;
    String userName;
    String profileUrl;
    MemberRole memberRole;

}
