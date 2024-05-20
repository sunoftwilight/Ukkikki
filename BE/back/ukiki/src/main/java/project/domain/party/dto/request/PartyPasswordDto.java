package project.domain.party.dto.request;

import lombok.Getter;

@Getter
public class PartyPasswordDto {

    private String beforePassword;
    private String afterPassword;
    private String simplePassword;

}
