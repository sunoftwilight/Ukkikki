package project.domain.chat.service;


import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.dto.response.SimpleChatDto;
import project.domain.chat.entity.Chat;
import project.domain.chat.mapper.ChatMapper;
import project.domain.chat.redis.ChatMember;
import project.domain.chat.repository.ChatRepository;
import project.domain.chat.repository.ChatMemberRedisRepository;
import project.domain.directory.service.DirectoryService;
import project.domain.member.dto.request.CustomOAuth2User;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberRole;
import project.domain.member.entity.Profile;
import project.domain.member.repository.MemberRepository;
import project.domain.member.repository.ProfileRepository;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;
import project.global.util.JasyptUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final SimpMessageSendingOperations template;
    private final PartyRepository partyRepository;
    private final ChatMemberRedisRepository chatMemberRedisRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ChatMapper chatMapper;
    private final JasyptUtil jasyptUtil;
    private final ProfileRepository profileRepository;

    @Transactional
    @Override
    public void sendChat(Long partyId, ChatDto chatDto, UserDetails userDetails) {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) userDetails;
//        Long memberId = customOAuth2User.getId();
        // TODO memberId 가져와야함
        Long memberId = 1L;

        Member member = memberRepository.findById(memberId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        // 파티 정보
        Party party = partyRepository.findById(partyId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        // 파티 전체 유저
        List<MemberParty> memberPartyList = party.getMemberPartyList()
            .stream()
            .filter(memberParty -> memberParty.getMemberRole() != MemberRole.BLOCK)
            .toList();

        //  채팅 작성자 프로필
        Profile profile = profileRepository.findByMemberIdAndPartyId(member.getId(), party.getId())
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_PROFILE));

        // 현재 채팅방에 들어와 있는 유저 목록
        List<ChatMember> chatMemberList = chatMemberRedisRepository.findAllByDestination("/sub/chats/party/"+ partyId);

        // 암호화 알고리즘
        StringEncryptor encryptor = jasyptUtil.customEncryptor(chatDto.getPassword());

        // 채팅 만들기
        Chat chat = Chat.customBuilder()
            .chatType(chatDto.getChatType())
            .content(jasyptUtil.keyEncrypt(encryptor, chatDto.getContent()))
            .member(member)
            .party(party)
            .build();
        chat.setUserName(profile.getNickname());
        chat.setProfile(profile);

        // 읽은 사람 목록 넣기
        for (ChatMember chatMember : chatMemberList) {
            memberRepository.findById(chatMember.getMemberId())
                .ifPresent(member1 -> chat.getReadMember().add(member1));
        }
        chatRepository.save(chat);

        SimpleChatDto simpleChatDto = chatMapper.toSimpleChatDto(chat);
        simpleChatDto.setContent(chatDto.getContent());
        simpleChatDto.setReadNum(memberPartyList.size() - chatMemberList.size());

        ResponseEntity<ResultResponse> res = ResponseEntity.ok(
            new ResultResponse(ResultCode.CHAT_SEND_SUCCESS, simpleChatDto));

        template.convertAndSend("/sub/chats/party/" + partyId, res);
    }
}
