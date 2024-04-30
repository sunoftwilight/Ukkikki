package project.domain.party.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeThumbDto {

    @NotNull
    Long partyId;
    @NotNull
    String key;
}
