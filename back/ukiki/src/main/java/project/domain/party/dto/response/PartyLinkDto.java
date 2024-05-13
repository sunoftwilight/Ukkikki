package project.domain.party.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PartyLinkDto {

    String partyLink;
    String partyName;
    Long party;
    String sseKey;
    String rootDirId;

}
