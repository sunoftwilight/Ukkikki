package project.domain.party.service;


import org.springframework.web.multipart.MultipartFile;
import project.domain.party.dto.request.CreateGroupDto;
import project.domain.party.dto.response.PartyLinkDto;

public interface PartyService {

    public PartyLinkDto createParty(CreateGroupDto createGroupDto, MultipartFile photo);
}
