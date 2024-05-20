package project.domain.party.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckPasswordDto {

    private Long partyId;
    private String sseKey;

}
