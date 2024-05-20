package project.domain.party.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterPartyDto {

    private String link;
    private String password;
    private String simplePassword;

}
