package project.domain.party.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreatePartyDto {

    @NotNull
    private String password;
    @NotNull
    private String partyName;
    @NotNull
    private String simplePassword;


}
