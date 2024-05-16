package project.domain.alarm.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import project.domain.alarm.AlarmIdentifier;
import project.domain.alarm.dto.request.AlarmPageableDto;
import project.domain.alarm.dto.response.AlarmPageDto;
import project.domain.alarm.dto.response.SimpleAlarm;
import project.domain.alarm.mapper.AlarmMapper;
import project.domain.alarm.redis.Alarm;
import project.domain.alarm.redis.AlarmType;
import project.domain.alarm.repository.AlarmRedisRepository;
import project.domain.alarm.repository.EmitterRepository;
import project.domain.member.dto.request.CustomOAuth2User;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.entity.Profile;
import project.domain.member.repository.MemberRepository;
import project.domain.member.repository.ProfileRepository;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
@Slf4j
public class AlarmServiceImpl implements AlarmService {

    private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 60;
    private final EmitterRepository emitterRepository;
    private final MemberpartyRepository memberpartyRepository;
    private final AlarmRedisRepository alarmRedisRepository;
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final AlarmMapper alarmMapper;




    @Override
    public Alarm createAlarm(AlarmType type, Long partyId, Long contentsId, Long targetId, Long writerId, String data){

        Party party = partyRepository.findById(partyId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        Profile profile = profileRepository.findByMemberIdAndPartyId(writerId, partyId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_PROFILE));

        List<String> identifier = new ArrayList<>();

        switch (type){
            case MEMO -> {
                identifier = AlarmIdentifier.MEMO.identifier(partyId, contentsId, targetId);
            }
            case REPLY -> {
                identifier = AlarmIdentifier.REPLY.identifier(partyId, contentsId, targetId);
            }
            case COMMENT -> {
                identifier = AlarmIdentifier.COMMENT.identifier(partyId, contentsId, targetId);
            }
            case CHECK -> {
                identifier = AlarmIdentifier.CHECK.identifier(partyId, contentsId, targetId);
            }
            case PASSWORD -> {
                identifier = AlarmIdentifier.PASSWORD.identifier(partyId, contentsId, targetId);
            }
            case CHAT -> {
                identifier = AlarmIdentifier.CHAT.identifier(partyId, contentsId, targetId);
            }
            case MENTION -> {
                identifier = AlarmIdentifier.MENTION.identifier(partyId, contentsId, targetId);
            }
        }
        return Alarm.builder()
            .partyId(partyId)
            .contentsId(contentsId)
            .targetId(targetId)
            .writerNick(profile.getNickname())
            .alarmType(type)
            .content(data)
            .partyUrl(party.getThumbnail())
            .partyName(party.getPartyName())
            .createDate(LocalDateTime.now().toString())
            .identifier(identifier)
            .build();
    }


    @Override
    public SseEmitter createEmitter(){

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        if (memberId == 0){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }

        // 생성했던 emitter 삭제
        emitterRepository.deleteAllEmitterStartWithId(memberId);

        String emitterId = makeEmitterId(memberId);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(emitterId, sseEmitter);

        // 자동삭제 설정
        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 연결유지 메시지 보내기
        sendAlarm(sseEmitter, memberId, Alarm.builder().alarmType(AlarmType.CHECK).build());

        return sseEmitter;
    }

    // 유저 아이디로 emitter 조회 / not exist -> return null;
    @Override
    public SseEmitter findEmitterByUserId(Long userId){
        return emitterRepository.getByUserId(userId);
    }

    @Override
    public void checkAlarm(String alarmId) {
        alarmRedisRepository.findById(alarmId)
            .ifPresent(alarm -> alarm.setIsRead(true));
    }

    /**
     * @param emitter   유저와 연결되있는 SSE : findEmitterByUserId() 함수에서 얻을 수 있다.
     * @param userId        Member id
     * @param alarm      메시지에 담기는 데이타 : 이동하는데 필요한 데이터가 들어간다
     */
    @Transactional
    @Override
    public void sendAlarm(SseEmitter emitter, Long userId, Alarm alarm){
        try{
            emitter.send(
                SseEmitter.event()
                .id(alarm.getAlarmType().toString())
                .name(String.valueOf(alarm.getAlarmType()))
                .data(alarmMapper.toSimpleAlarm(alarm))
            );
        }catch (IOException e){
            String emitterKey = emitterRepository.getEmitterKeyByUserId(userId);
            emitterRepository.deleteById(emitterKey);
        }
    }
    @Transactional
    @Override
    public void groupSendAlarm(Alarm alarm, Long senderId) {
        List<MemberParty> memberPartyList = memberpartyRepository.findMemberList(alarm.getPartyId());
//        Alarm data = createAlarm(type, partyId, contentsId, targetId, writerId,content);
        for (MemberParty memberParty : memberPartyList) {
            Long sendMemberId = memberParty.getMember().getId();
            if(sendMemberId.equals(senderId)){
                continue;
            }

            Alarm saveAlarm = new Alarm(alarm, sendMemberId);
            alarmRedisRepository.save(saveAlarm);
            SseEmitter memberEmitter = emitterRepository.getByUserId(sendMemberId);
            if(memberEmitter != null){
                sendAlarm(memberEmitter, sendMemberId, saveAlarm);
            }
        }
    }
    // 모든 SseEmitter의 생존 여부를 판단해주는 함수
    @Override
    public void checkEmitterLive() {
        Map<String, SseEmitter> allEmitter = emitterRepository.allEmitter();
        for (Map.Entry<String, SseEmitter> entry : allEmitter.entrySet()) {
            sendAlarm(entry.getValue(), Long.valueOf(entry.getKey().split("_")[0]), Alarm.builder().alarmType(AlarmType.CHECK).build());
        }
    }

    @Override
    public AlarmPageDto getAlarmList(AlarmPageableDto alarmPageableDto) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        memberRepository.findById(memberId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        PageRequest pageable = PageRequest.of(alarmPageableDto.getPageNo()-1, alarmPageableDto.getPageSize()+1, Sort.by(Sort.Direction.ASC, "createDate"));

        Page<Alarm> alarmPage = alarmRedisRepository.findAllByMemberId(memberId, pageable);
        List<SimpleAlarm> simpleAlarmList = alarmPage.stream()
            .map(alarmMapper::toSimpleAlarm)
            .toList();
        List<SimpleAlarm> alarmList = new ArrayList<>(simpleAlarmList);
        AlarmPageDto res = AlarmPageDto.builder()
            .pageNo(alarmPageableDto.getPageNo())
            .pageSize(alarmPageableDto.getPageSize())
            .build();

        if(alarmList.size() == alarmPageableDto.getPageSize()+1){
            alarmList.removeLast();
        }else{
            res.setLast(true);
        }
        res.setAlarmList(alarmList);

        return res;
    }

    // Emitter Id를 만들어 준다
    public String makeEmitterId(Long userId){
        return userId + "_" + System.currentTimeMillis();
    }

}
