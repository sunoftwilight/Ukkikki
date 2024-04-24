package project.domain.party.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberParty;
import project.domain.member.entity.MemberRole;
import project.domain.member.repository.MemberRepository;
import project.domain.party.dto.request.CreateGroupDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.entity.Party;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.redis.PartyLink;

import project.domain.party.repository.PartyLinkRedisRepository;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.BcryptUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@AllArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final MemberpartyRepository memberpartyRepository;
    private final PartyLinkRedisRepository partyLinkRedisRepository;


    private final BcryptUtil bcryptUtil;

    @Override
    public PartyLinkDto createParty(CreateGroupDto createGroupDto, MultipartFile photo) {
        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow (() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        log.info(createGroupDto.getPartyName());
        log.info(createGroupDto.getPassword());

        Pattern namePattern = Pattern.compile("^[0-9a-zA-Z가-힣\\/!\\-_.*\\'\\(\\)\\s]{1,10}$");	// 따옴표 안에 있는 패턴 추출.
        Matcher matcher = namePattern.matcher(createGroupDto.getPartyName());
        if (!matcher.matches()){
            throw new BusinessLogicException(ErrorCode.PARTY_NAME_INVALID);
        }

        Pattern passwordPattern = Pattern.compile("^[0-9a-zA-Z\\!@#$%^*+=-]{8,15}$");	// 따옴표 안에 있는 패턴 추출.
        Matcher matcher2 = passwordPattern.matcher(createGroupDto.getPassword());
        if (!matcher2.matches()) {
            throw new BusinessLogicException(ErrorCode.PARTY_PASSWORD_INVALID);
        }

            // TODO 이미지 저장 해야함
        // String partyThumbnail = imageUploader.upload(photo);
        String partyThumbnail = "TEST-ADDRESS";

        Party party = Party.builder()
            .partyName(createGroupDto.getPartyName())
            .thumbnail(partyThumbnail)
            .password(bcryptUtil.encodeBcrypt(createGroupDto.getPassword()))
            .build();
        partyRepository.save(party);

        MemberParty memberParty = MemberParty.builder()
            .memberRole(MemberRole.MASTER)
            .party(party)
            .member(member)
            .build();
        memberpartyRepository.save(memberParty);

//        //TODO Redis에 링크 저장
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHourLater = now.plusHours(2);
//
        PartyLink partyLink = PartyLink.builder()
            .partyId(party.getId())
            .partyLink(makeLink())
            .deadLine(twoHourLater)
            .build();
//        partyLinkRedisRepository.save(partyLink);

        return new PartyLinkDto("http://localhost:8081/api/party/enter/"+party.getId()+"/"+partyLink.getPartyLink());
    }






    public String makeLink(){

        Map<Integer, List<Integer>> numsRange = new HashMap<>(){{
            put(0, new ArrayList<>(List.of(10, 48)));
            put(1, new ArrayList<>(List.of(26, 65)));
            put(2, new ArrayList<>(List.of(26, 97)));
        }};

        String returnValue = "";
        for (int i= 0; i < 10; i++){
            int j = (int)(Math.random() * 10) % 3;
            List<Integer> nums = numsRange.get(j);
            String word = String.valueOf((char)(((int)(Math.random() * nums.getFirst()) + nums.getLast()) ));
            returnValue = returnValue.concat(word);
        }

        return returnValue;
    }
}
