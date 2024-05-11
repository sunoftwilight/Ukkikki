package project.domain.party.dto.response;


import lombok.Getter;
import lombok.Setter;
import project.domain.member.dto.response.SimpleProfileDto;
import java.util.List;

@Getter
@Setter
public class PartyDto {

    private String partyName;

    private String thumbnail;

    private String rootDirId;

    private List<SimpleProfileDto> partyMembers;

}
