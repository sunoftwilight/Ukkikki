package project.domain.party.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeThumbDto {

    String partyName;

    @NotNull
    String  simplePassword;

    @NotNull
    String key;
}
