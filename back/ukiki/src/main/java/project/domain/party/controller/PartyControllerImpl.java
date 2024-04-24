package project.domain.party.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import project.domain.party.dto.request.CreateGroupDto;
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
        return ResponseEntity.ok(new ResultResponse(ResultCode.GET_USERLIST_SUCCESS,
            partyService.createParty(createGroupDto, photo)));
    }
}
