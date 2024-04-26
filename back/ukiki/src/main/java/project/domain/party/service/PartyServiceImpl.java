package project.domain.party.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.domain.article.redis.Alarm;
import project.domain.article.redis.AlarmType;
import project.domain.article.repository.AlarmRedisRepository;
import project.domain.member.entity.Member;
import project.domain.party.dto.request.EnterPartyDto;
import project.domain.party.dto.request.PartyPasswordDto;
import project.domain.party.dto.response.PartyEnterDto;
import project.domain.party.entity.MemberParty;
import project.domain.member.entity.MemberRole;
import project.domain.member.repository.MemberRepository;
import project.domain.party.dto.request.CreatePartyDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.entity.Party;
import project.domain.party.mapper.PartyLinkMapper;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.redis.PartyLink;

import project.domain.party.repository.PartyLinkRedisRepository;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.BcryptUtil;

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
    private final AlarmRedisRepository alarmRedisRepository;
    private final PartyLinkMapper partyLinkMapper;

    private final BcryptUtil bcryptUtil;

    @Override
    @Transactional
    public PartyLinkDto createParty(CreatePartyDto createPartyDto, MultipartFile photo) {
        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        // 파티 이름 규칙 확인
        checkPartyName(createPartyDto.getPartyName());
        
        // 비밀번호 규칙 확인
        checkPassword(createPartyDto.getPassword());

        // TODO 이미지 저장 해야함
        // String partyThumbnail = imageUploader.upload(photo);
        String partyThumbnail = "TEST-ADDRESS";

        Party party = Party.builder()
            .partyName(createPartyDto.getPartyName())
            .thumbnail(partyThumbnail)
            .password(bcryptUtil.encodeBcrypt(createPartyDto.getPassword()))
            .build();
        partyRepository.save(party);

        MemberParty memberParty = MemberParty.builder()
            .memberRole(MemberRole.MASTER)
            .party(party)
            .member(member)
            .build();
        memberpartyRepository.save(memberParty);

//        //TODO Redis에 링크 저장
        String link = makeLink(); // 고유한 link가 나오도록 반복
//        while (partyLinkRedisRepository.findById(link).isPresent()){
//            link = makeLink();
//        }

        PartyLink partyLink = PartyLink.builder()
            .partyLink(link)
            .party(party)
            .build();

//        partyLinkRedisRepository.save(partyLink);

        return partyLinkMapper.toPartyLinkDto(partyLink);
    }

    @Override
    @Transactional
    public PartyLinkDto createLink(Long partyId) {

        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        memberpartyRepository.findByMemberAndParty(member, party)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR));


        // TODO Redis 연결 안됨
        // 기존 경로는 삭제
        Optional<PartyLink> existLink = partyLinkRedisRepository.findByParty(party);
        existLink.ifPresent(partyLinkRedisRepository::delete);

        String link = makeLink(); // 고유한 link가 나오도록 반복
//        while (partyLinkRedisRepository.findById(link).isPresent()){
//            link = makeLink();
//        }

        PartyLink partyLink = PartyLink.builder()
            .partyLink(link)
            .party(party)
            .build();

//        partyLinkRedisRepository.save(partyLink);

        return partyLinkMapper.toPartyLinkDto(partyLink);
    }

    @Override
    @Transactional(readOnly = true)
    public void enterParty(String link) {
        // redis에 없는 파티 참여 링크라면 에러 반환
        partyLinkRedisRepository.findById(link)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_LINK_INVALID));
    }

    @Override
    @Transactional
    public void checkPassword(EnterPartyDto enterPartyDto) {
        PartyLink partyLink = partyLinkRedisRepository.findById(enterPartyDto.getLink())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_LINK_INVALID));

        // 비밀번호 비교
        if (!bcryptUtil.matchesBcrypt(enterPartyDto.getPassword(), partyLink.getParty().getPassword())) {
            if (partyLink.getCount() == 1) {   // 카운트를 다 사용했으면 링크 제거
                partyLinkRedisRepository.delete(partyLink);
                throw new BusinessLogicException(ErrorCode.INPUT_NUMBER_EXCEED);
            }
            partyLink.setCount(partyLink.getCount() - 1); // 카운트 -1 하기
            partyLinkRedisRepository.save(partyLink);

            throw new BusinessLogicException(ErrorCode.PARTY_PASSWORD_INVALID);
        }
    }

    @Override
    @Transactional
    public PartyEnterDto memberPartyEnter(EnterPartyDto enterPartyDto) {
        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));

        PartyLink partyLink = partyLinkRedisRepository.findById(enterPartyDto.getLink())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_LINK_INVALID));

        // 파티에 유저 뷰어 권한으로 넣어주기
        MemberParty memberParty = MemberParty.builder()
            .party(partyLink.getParty())
            .member(member)
            .memberRole(MemberRole.VIEWER)
            .build();

        memberpartyRepository.save(memberParty);

        PartyEnterDto res = partyLinkMapper.toPartyEnterDto(partyLink);
        return res;
    }

    @Override
    public PartyEnterDto guestPartyEnter(EnterPartyDto enterPartyDto) {
        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));


        PartyLink partyLink = partyLinkRedisRepository.findById(enterPartyDto.getLink())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_LINK_INVALID));

        //TODO 게스트용 토큰 만들어야함.
        String guestToken = "asd2123_asd1kas1+asd";


        PartyEnterDto res = partyLinkMapper.toPartyEnterDto(partyLink);
        res.setToken(guestToken);
        return res;
    }

    @Override
    public void changePassword(Long partyId, PartyPasswordDto partyPasswordDto) {
        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        // 파티확인
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 내 권한 존재 확인
        MemberParty memberParty = memberpartyRepository.findByMemberAndParty(member, party)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR));

        // 마스터 권한 확인
        if(!memberParty.getMemberRole().equals(MemberRole.MASTER)){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_MASTER);
        }

        // 기존 비밀번호가 확인
        if (!bcryptUtil.matchesBcrypt(partyPasswordDto.getBeforePassword(), party.getPassword())) {
            throw new BusinessLogicException(ErrorCode.PARTY_PASSWORD_INVALID);
        }
        // 바뀔 비밀번호 유효성 체크
        checkPassword(partyPasswordDto.getAfterPassword());
        
        // 바뀐 party에 비밀번호 적용
        party.setPassword(bcryptUtil.encodeBcrypt(partyPasswordDto.getAfterPassword()));
        partyRepository.save(party);
        
        // 알람 보내기
        List<MemberParty> memberParties = memberpartyRepository.findAllByParty(party);
        for (MemberParty memberParty1 : memberParties){
            // 마스터라면 넘어가기
            if (memberParty1.getMemberRole().equals(MemberRole.MASTER)){
                continue;
            }
            Alarm alarm = Alarm.builder()
                .partyId(party.getId())
                .memberId(member.getId())
                .alarmType(AlarmType.PASSWORD)
                .content("비밀번호가 변경 되었습니다.")
                .identifier(String.valueOf(party.getId()))
                .build();
            alarmRedisRepository.save(alarm);
            //TODO SSE 보내기 . . .
        }

        //TODO S3 이미지 암호 키 바꾸기 ( partyPasswordDto.getAfterPassword() 데이터로)

    }

    @Override
    @Transactional
    public void changePartyName(Long partyId, String partyName) {

        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        // 파티확인
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 그룹원 + 마스터 여부 확인
        MemberParty memberParty = memberpartyRepository.findByMemberAndParty(member, party)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR));
        
        if (!memberParty.getMemberRole().equals(MemberRole.MASTER)){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_MASTER);
        }
        // 이름 체크
        checkPartyName(partyName);

        party.setPartyName(partyName);
        partyRepository.save(party);
    }

    @Override
    public void grantPartyUser(Long partyId, Long opponentId, MemberRole memberRole) {

        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        // 파티확인
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 마스터권한 확인
        MemberParty memberParty = memberpartyRepository.findByMemberAndPartyAndMemberRoleIs(member, party, MemberRole.MASTER)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_ROLE_MASTER));
        // 타겟 권한 가지고 오기
        MemberParty targetMemberParty = memberpartyRepository.findByMemberAndParty(member, party)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_EXIST_PARTY_USER));

        // 마스터 권한 부여할 때 -> 사용자 에디터로 변경 저장
        if (memberRole.equals(MemberRole.MASTER)){
            memberParty.setMemberRole(MemberRole.EDITOR);
            memberpartyRepository.save(memberParty);
        }
        
        // 권한 변경 후 저장
        targetMemberParty.setMemberRole(memberRole);
        memberpartyRepository.save(targetMemberParty);
    }


    public String makeLink() { // 링크를 만들어 주는

        Map<Integer, List<Integer>> numsRange = new HashMap<>() {{
            put(0, new ArrayList<>(List.of(10, 48)));
            put(1, new ArrayList<>(List.of(26, 65)));
            put(2, new ArrayList<>(List.of(26, 97)));
        }};

        String returnValue = "";
        for (int i = 0; i < 10; i++) {
            int j = (int) (Math.random() * 10) % 3;
            List<Integer> nums = numsRange.get(j);
            String word = String.valueOf((char) (((int) (Math.random() * nums.getFirst()) + nums.getLast())));
            returnValue = returnValue.concat(word);
        }

        return returnValue;
    }
    
    // 비밀번호 유효성 확인 함수
    public void checkPassword(String password){ 
        Pattern passwordPattern = Pattern.compile("^[0-9a-zA-Z\\!@#$%^*+=-]{8,15}$");    // 따옴표 안에 있는 패턴 추출.
        Matcher matcher2 = passwordPattern.matcher(password);
        if (!matcher2.matches()) {
            throw new BusinessLogicException(ErrorCode.PARTY_PASSWORD_INVALID);
        }
    }

    // 파티이름 유효성 확인 함수
    public void checkPartyName(String partyName){
        Pattern namePattern = Pattern.compile("^[0-9a-zA-Z가-힣\\\\/!\\\\-_.*'()\\\\s]{1,10}$");    // 따옴표 안에 있는 패턴 추출.
        Matcher matcher = namePattern.matcher(partyName);
        if (!matcher.matches()) {
            throw new BusinessLogicException(ErrorCode.PARTY_NAME_INVALID);
        }
    }
}
