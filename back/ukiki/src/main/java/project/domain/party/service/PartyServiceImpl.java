package project.domain.party.service;


import com.amazonaws.services.s3.model.SSECustomerKey;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.domain.article.redis.Alarm;
import project.domain.article.redis.AlarmType;
import project.domain.article.repository.AlarmRedisRepository;
import project.domain.directory.service.DirectoryService;
import project.domain.member.entity.Member;
import project.domain.party.dto.request.ChangeThumbDto;
import project.domain.party.dto.request.EnterPartyDto;
import project.domain.party.dto.request.PartyPasswordDto;
import project.domain.party.dto.response.PartyEnterDto;
import project.domain.party.dto.response.SimpleMemberPartyDto;
import project.domain.party.entity.MemberParty;
import project.domain.member.entity.MemberRole;
import project.domain.member.repository.MemberRepository;
import project.domain.party.dto.request.CreatePartyDto;
import project.domain.party.dto.response.PartyLinkDto;
import project.domain.party.entity.Party;
import project.domain.party.mapper.MemberPartyMapper;
import project.domain.party.mapper.PartyLinkMapper;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.redis.PartyLink;

import project.domain.party.repository.PartyLinkRedisRepository;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.PhotoUrl;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.BcryptUtil;
import project.global.util.S3Util;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final DirectoryService directoryService;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final MemberpartyRepository memberpartyRepository;
    private final PartyLinkRedisRepository partyLinkRedisRepository;
    private final AlarmRedisRepository alarmRedisRepository;
    private final PhotoRepository photoRepository;
    private final PartyLinkMapper partyLinkMapper;
    private final MemberPartyMapper memberPartyMapper;
    private final S3Util s3Util;
    private final BcryptUtil bcryptUtil;
    private final RedisTemplate redisTemplate;

    @Override
    @Transactional
    public PartyLinkDto createParty(CreatePartyDto createPartyDto, MultipartFile photo) {
        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        // 파티 이름 규칙 확인
        checkPartyName(createPartyDto.getPartyName());

        // 비밀번호 규칙 확인
        checkPassword(createPartyDto.getPassword());

        Party party = Party.builder()
            .partyName(createPartyDto.getPartyName())
            .password(bcryptUtil.encodeBcrypt(createPartyDto.getPassword()))
            .build();

        // 이미지 저장 해야함

        if (photo != null){
            String partyThumbnailImg = s3Util.fileUpload(photo,
                new SSECustomerKey(s3Util.generateSSEKey(createPartyDto.getPassword())));
            party.setThumbnail(partyThumbnailImg);
        }
        partyRepository.save(party);

        // 해당 파티에 초기 공유 앨범 디렉토리 부여
        directoryService.initDirParty(party);

        MemberParty memberParty = MemberParty.customBuilder()
            .memberRole(MemberRole.MASTER)
            .party(party)
            .member(member)
            .build();
        memberpartyRepository.save(memberParty);

//        //TODO Redis에 링크 저장
        String link = makeLink(); // 고유한 link가 나오도록 반복
        while (partyLinkRedisRepository.findById(link).isPresent()){
            link = makeLink();
        }

        PartyLink partyLink = PartyLink.builder()
            .partyLink(link)
            .party(party.getId())
            .build();

        partyLinkRedisRepository.save(partyLink);
        log.info("TEST HERE??");
        return partyLinkMapper.toPartyLinkDto(partyLink);
    }

    @Override
    @Transactional
    public PartyLinkDto createLink(Long partyId) {

        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        memberpartyRepository.findByMemberAndParty(member, party)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR));

        // 기존 경로는 삭제
        Optional<PartyLink> existLink = partyLinkRedisRepository.findByParty(party.getId());
        log.info("TEST1");
        existLink.ifPresent(link ->
        {
            log.info(link.getCount() + " ");
            partyLinkRedisRepository.delete(link);
        });
        log.info("TEST2");

        String link = makeLink(); // 고유한 link가 나오도록 반복
        while (partyLinkRedisRepository.findById(link).isPresent()){
            link = makeLink();
        }

        PartyLink partyLink = PartyLink.builder()
            .partyLink(link)
            .party(party.getId())
            .build();

        partyLinkRedisRepository.save(partyLink);

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
        PartyLink partyLink = partyLinkRedisRepository.findByPartyLink(enterPartyDto.getLink())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_LINK_INVALID));
        log.info("TEST LINK SUCESS");
        // 파티확인
        Party party = partyRepository.findById(partyLink.getParty())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        log.info("TEST PARTY SUCESS");
        // 비밀번호 비교
        if (!bcryptUtil.matchesBcrypt(enterPartyDto.getPassword(), party.getPassword())) {
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
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        PartyLink partyLink = partyLinkRedisRepository.findById(enterPartyDto.getLink())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_LINK_INVALID));
// 파티확인
        Party party = partyRepository.findById(partyLink.getParty())
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 파티에 유저 찾고 없으면 새로 만들기
        MemberParty memberParty = memberpartyRepository.findByMemberAndParty(member, party)
            .orElseGet(() -> MemberParty.customBuilder()
                .party(party)
                .member(member)
                .memberRole(MemberRole.VIEWER)
                .build());
        // 차단 당한 유저라면?
        if (memberParty.getMemberRole().equals(MemberRole.BLOCK)){
            throw new BusinessLogicException(ErrorCode.ENTER_DENIED_BLOCK_USER);
        }

        memberpartyRepository.save(memberParty);

        PartyEnterDto res = partyLinkMapper.toPartyEnterDto(partyLink);
        return res;
    }

    @Override
    public PartyEnterDto guestPartyEnter(EnterPartyDto enterPartyDto) {
        // TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));


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
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // 파티확인
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 내 권한 존재 확인
        MemberParty memberParty = memberpartyRepository.findByMemberAndParty(member, party)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR));

        // 마스터 권한 확인
        if (!memberParty.getMemberRole().equals(MemberRole.MASTER)) {
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
        for (MemberParty memberParty1 : memberParties) {
            // 마스터라면 넘어가기
            if (memberParty1.getMemberRole().equals(MemberRole.MASTER)) {
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
        // S3 이미지 비밀번호 바꾸기
        List<Photo> photos = party.getPhotoList();
        for (Photo photo : photos) {
            for (String url : photo.getPhotoUrl().photoUrls()){
                String fileName = url.substring(
                    url.lastIndexOf("/"),
                    url.lastIndexOf(".") - 1);
                s3Util.changeKey(partyPasswordDto.getBeforePassword(), partyPasswordDto.getAfterPassword(), fileName);
            }
        }
        


    }

    @Override
    @Transactional
    public void changePartyName(Long partyId, String partyName) {

        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // 파티확인
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));
        // 그룹원 + 마스터 여부 확인
        MemberParty memberParty = memberpartyRepository.findByMemberAndParty(member, party)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR));

        if (!memberParty.getMemberRole().equals(MemberRole.MASTER)) {
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
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
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
        if (memberRole.equals(MemberRole.MASTER)) {
            memberParty.setMemberRole(MemberRole.EDITOR);
            memberpartyRepository.save(memberParty);
        }

        // 권한 변경 후 저장
        targetMemberParty.setMemberRole(memberRole);
        memberpartyRepository.save(targetMemberParty);
    }

    @Override
    public void exitParty(Long partyId, String key) {
        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        Member member = memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        MemberParty memberParty = memberpartyRepository.findByMemberIdAndPartyId(member.getId(), partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_EXIST_PARTY_USER));

        // 파티에 남아있는 유저 수
        int partyMemberCount = memberpartyRepository.countAllByPartyIdAndMemberRoleIsNot(partyId, MemberRole.VIEWER);

        // 1명 이상이 존재하고 마스터라면 불가능
        if (partyMemberCount != 1 && memberParty.getMemberRole().equals(MemberRole.MASTER)) {
            throw new BusinessLogicException(ErrorCode.MASTER_CANT_EXIT);
        }

        // 자신 파티에서 삭제
        memberpartyRepository.delete(memberParty);

        // 자신 밖에 없을 때 party 데이터 삭제
        if (partyMemberCount == 1) {
            // S3 key
            SSECustomerKey sseKey = new SSECustomerKey(s3Util.generateSSEKey(key));

            Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

            // 파티 썸네일 삭제
            String thumbnail = party.getThumbnail();
            s3Util.fileDelete(sseKey, thumbnail.substring(
                thumbnail.lastIndexOf("/"),
                thumbnail.lastIndexOf(".") - 1));
            // 파티 내 사진 삭제
            List<Photo> photos = party.getPhotoList();
            for (Photo photo : photos) {
                for (String url : photo.getPhotoUrl().photoUrls()){
                    String fileName = url.substring(
                        url.lastIndexOf("/"),
                        url.lastIndexOf(".") - 1);
                    s3Util.fileDelete(sseKey, fileName);
                }
            }
            // 파티 삭제
            partyRepository.delete(party);

            // TODO directory, comment 삭제 코드 추가해야함
        }

    }

    @Override
    public void memberBlock(Long partyId, Long targetId) {
        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        memberpartyRepository.findByMemberIdAndPartyIdAndMemberRoleIs(1L, partyId, MemberRole.MASTER)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_ROLE_MASTER));

        MemberParty targetParty = memberpartyRepository.findByMemberIdAndPartyId(targetId, partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // 멤버 차단 시켜 버리기
        targetParty.setMemberRole(MemberRole.BLOCK);
    }

    @Override
    public void kickMember(Long partyId, Long targetId) {
        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // 마스터 권한 확인
        memberpartyRepository.findByMemberIdAndPartyIdAndMemberRoleIs(1L, partyId, MemberRole.MASTER)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_ROLE_MASTER));
        // 상대방 찾기
        MemberParty targetParty = memberpartyRepository.findByMemberIdAndPartyId(targetId, partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // 상대방 삭제
        memberpartyRepository.delete(targetParty);
        
    }

    @Override
    public List<SimpleMemberPartyDto> getBlockUserList(Long partyId) {
        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // 마스터 권한 확인
        memberpartyRepository.findByMemberIdAndPartyIdAndMemberRoleIs(1L, partyId, MemberRole.MASTER)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_ROLE_MASTER));

        // BLOCK USER 조회
        List<MemberParty> memberPartyList = memberpartyRepository.findAllByPartyIdAndMemberRoleIs(partyId, MemberRole.BLOCK);
        List<SimpleMemberPartyDto> res = memberPartyMapper.toSimplePartyMemberDtoList(memberPartyList);

        return res;
    }

    @Override
    public List<SimpleMemberPartyDto> getUserList(Long partyId) {
        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        memberpartyRepository.findByMemberIdAndPartyId(1L, partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_EXIST_PARTY_USER));

        // Party User 조회
        List<MemberParty> memberPartyList = memberpartyRepository.findMemberList(partyId);
        List<SimpleMemberPartyDto> res = memberPartyMapper.toSimplePartyMemberDtoList(memberPartyList);

        return res;
    }

    @Override
    public void changePartyThumb(Long partyId, ChangeThumbDto changeThumbDto, MultipartFile photo) {
        // 유저확인 TODO 유저 아이디를 토큰에서 받아야 함
        memberRepository.findById(1L)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // 마스터 권한 확인
        MemberParty memberParty = memberpartyRepository.findByMemberIdAndPartyIdAndMemberRoleIs(1L, partyId, MemberRole.MASTER)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_ROLE_MASTER));
        // 암호 확인
        if (!bcryptUtil.matchesBcrypt(changeThumbDto.getKey(), memberParty.getParty().getPassword())){
            throw new BusinessLogicException(ErrorCode.PARTY_PASSWORD_INVALID);
        }

        // 썸네일 변경
        String thumbUrl = memberParty.getParty().getThumbnail();
        String generatedKey = s3Util.generateSSEKey(changeThumbDto.getKey());
        SSECustomerKey sseKey = new SSECustomerKey(generatedKey);
        s3Util.fileDelete(sseKey, thumbUrl.substring(
            thumbUrl.lastIndexOf("/"),
            thumbUrl.lastIndexOf(".") - 1));
        String newThumbUrl = s3Util.fileUpload(photo, sseKey);

        memberParty.getParty().setThumbnail(newThumbUrl);

    }

    @Override
    public void linkDelete() {
        String linkPattern = "partyLink:*:idx";
        String idPattern = "partyLink:party:*";

        // 패턴으로 해당 set KEY값 추출 -> Map으로 바꿔준다.
        Set<String> linkkeys = redisTemplate.keys(linkPattern);
        Map<String, String> linkKeyMap = linkkeys.stream()
            .collect(Collectors.toMap(
                key -> key.split(":")[1], // splitValue
                Function.identity() // key
            ));

        Set<String> idKeys = redisTemplate.keys(idPattern);
        Map<Long, String> idMap = idKeys.stream()
            .collect(Collectors.toMap(
                key -> Long.parseLong(key.split(":")[2]),
                Function.identity()
            ));
        List<String> linkList = new ArrayList<>(linkKeyMap.keySet());
        List<Long> idList = new ArrayList<>(idMap.keySet());
        // 해당 key를 돌면서 hash 값이 없다면 삭제 -> ttl로 자동제거가 안됨
        for (int i=0; i< linkList.size();i++) {

            Optional<PartyLink> partyLink = partyLinkRedisRepository.findByPartyLink(linkList.get(i));
            if(!partyLink.isPresent()){
                try {
                    redisTemplate.delete(linkKeyMap.get(linkList.get(i)));
                    redisTemplate.opsForSet().remove("partyLink", linkList.get(i));
                }catch (Exception ignore){}
            }
            Optional<PartyLink> partyId = partyLinkRedisRepository.findByParty(idList.get(i));
            if(!partyId.isPresent()){
                try {
                    redisTemplate.delete(idMap.get(idList.get(i)));

                }catch (Exception ignore){}
            }
        }
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
    public void checkPassword(String password) {
        Pattern passwordPattern = Pattern.compile("^[0-9a-zA-Z\\!@#$%^*+=-]{8,15}$");    // 따옴표 안에 있는 패턴 추출.
        Matcher matcher2 = passwordPattern.matcher(password);
        if (!matcher2.matches()) {
            throw new BusinessLogicException(ErrorCode.PARTY_PASSWORD_INVALID);
        }
    }

    // 파티이름 유효성 확인 함수
    public void checkPartyName(String partyName) {
        Pattern namePattern = Pattern.compile("^[0-9a-zA-Z가-힣\\\\/!\\\\-_.*'()\\\\s]{1,10}$");    // 따옴표 안에 있는 패턴 추출.
        Matcher matcher = namePattern.matcher(partyName);
        if (!matcher.matches()) {
            throw new BusinessLogicException(ErrorCode.PARTY_NAME_INVALID);
        }
    }
}
