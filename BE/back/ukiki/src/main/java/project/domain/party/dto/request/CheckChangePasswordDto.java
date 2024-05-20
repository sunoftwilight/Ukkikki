package project.domain.party.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckChangePasswordDto {

    private String password;
    private Long partyId;
    private String simplePassword;

}
