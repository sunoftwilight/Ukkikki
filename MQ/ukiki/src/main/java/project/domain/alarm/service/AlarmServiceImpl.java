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
import project.domain.party.repository.MemberpartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

import java.io.IOException;
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
    private final AlarmMapper alarmMapper;




    @Override
    public Alarm createAlarm(AlarmType type, Long partyId, Long articleId, Long targetId, String data){

        Profile profile = profileRepository.findByMemberIdAndPartyId(targetId, partyId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_PROFILE));

        List<String> identifier = new ArrayList<>();
        String message = null;
        switch (type){
            case CHAT -> {
                identifier = AlarmIdentifier.CHAT.identifier(partyId, articleId);
            }
            case REPLY -> {
                message = String.format("%s님께서 회원님에게 댓글을 작성하였습니다.\n%s", profile.getNickname(), data);
                identifier = AlarmIdentifier.REPLY.identifier(partyId, articleId);
            }
            case ARTICLE -> {
                message = String.format("%s 게시글이 생성 되었습니다.", data);
                identifier = AlarmIdentifier.ARTICLE.identifier(partyId, articleId);
            }
            case COMMENT -> {
                message = String.format("%s님께서 댓글을 작성하였습니다.\n%s", "홍길동", data);
                identifier = AlarmIdentifier.COMMENT.identifier(partyId, articleId);
            }
            case PASSWORD -> {
                message = String.format("%s 그룹의 비밀번호가 변경되었습니다.", data);
            }
            case CHECK -> {
                message = "체크 알람";
            }
        }
        Alarm res = Alarm.builder()
            .alarmType(type)
            .partyId(partyId)
            .articleId(articleId)
            .targetId(targetId)
            .content(message)
            .identifier(identifier)
            .build();
        return res;
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

    /**
     * @param emitter   유저와 연결되있는 SSE : findEmitterByUserId() 함수에서 얻을 수 있다.
     * @param userId        Member id
     * @param alarm      메시지에 담기는 데이타 : 이동하는데 필요한 데이터가 들어간다
     */
    @Transactional
    @Override
    public void sendAlarm(SseEmitter emitter, Long userId, Alarm alarm){
        try{
            emitter.send(SseEmitter.event()
                .id(makeEmitterId(userId))
                .name(String.valueOf(alarm.getAlarmType()))
                .data(alarmMapper.toSimpleAlarm(alarm))
            );
        }catch (IOException e){
            alarm.setIsRead(false);
            String emitterKey = emitterRepository.getEmitterKeyByUserId(userId);
            emitterRepository.deleteById(emitterKey);
        }
    }
    @Transactional
    @Override
    public void groupSendAlarm(Long memberId, AlarmType type, Long partyId, Long articleId, Long targetId) {
        List<MemberParty> memberPartyList = memberpartyRepository.findMemberList(partyId);
        Alarm data = createAlarm(type, partyId, articleId, targetId, "");
        for (MemberParty memberParty : memberPartyList) {
            Long sendMemberId = memberParty.getMember().getId();
            if(sendMemberId.equals(memberId)){
                continue;
            }

            Alarm alarm = new Alarm(data, sendMemberId);
            alarmRedisRepository.save(alarm);
            SseEmitter memberEmitter = emitterRepository.getByUserId(sendMemberId);
            if(memberEmitter != null){
                sendAlarm(memberEmitter, sendMemberId, alarm);
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

        Pageable pageable = PageRequest.of(alarmPageableDto.getPageNo()-1, alarmPageableDto.getPageSize()+1, Sort.Direction.DESC, "createDate");

        Page<Alarm> alarmPage = alarmRedisRepository.findAllByMemberId(memberId, pageable);
        List<SimpleAlarm> alarmList = alarmPage.stream()
            .map(alarmMapper::toSimpleAlarm)
            .toList();

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
