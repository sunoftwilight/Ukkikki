package project.domain.party.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import project.domain.party.dto.request.CreateGroupDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.service.PartyService;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;


@RequestMapping("/party")
@RestController
@RequiredArgsConstructor
public class PartyControllerImpl implements PartyController{

    private final PartyService partyService;

    @Override
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> createParty(@RequestPart @Valid CreateGroupDto createGroupDto, @RequestPart(required = false) MultipartFile photo) {
        PartyLinkDto response = partyService.createParty(createGroupDto, photo);
        return ResponseEntity.ok(new ResultResponse(ResultCode.CREATE_PARTY_SUCCESS, response));
    }

    @Override
    @GetMapping("/link/{partyId}")
    public ResponseEntity<ResultResponse> makePartyLink(@PathVariable Long partyId) {
        PartyLinkDto response = partyService.createLink(partyId);
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_PARTY_LINK_SUCCESS, response));
    }
}
