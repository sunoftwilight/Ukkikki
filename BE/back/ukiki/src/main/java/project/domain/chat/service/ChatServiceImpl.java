package project.domain.chat.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.dto.request.ChatPagealbeDto;
import project.domain.chat.dto.response.ChatPageDto;
import project.domain.chat.dto.response.SimpleChatDto;
import project.domain.chat.entity.Chat;
import project.domain.chat.entity.ChatType;
import project.domain.chat.mapper.ChatMapper;
import project.domain.chat.redis.ChatMember;
import project.domain.chat.repository.ChatRepository;
import project.domain.chat.repository.ChatMemberRedisRepository;
import project.domain.directory.service.DirectoryService;
import project.domain.member.dto.request.CustomOAuth2User;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.dto.response.KeyGroupDto;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberRole;
import project.domain.member.entity.Profile;
import project.domain.member.repository.MemberRepository;
import project.domain.member.repository.ProfileRepository;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.repository.PartyRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.jwt.JWTUtil;
import project.global.result.ResultCode;
import project.global.result.ResultResponse;
import project.global.util.JasyptUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final PartyRepository partyRepository;
    private final ChatMemberRedisRepository chatMemberRedisRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final MemberpartyRepository memberpartyRepository;

    private final ChatMapper chatMapper;

    private final SimpMessageSendingOperations template;
    private final JasyptUtil jasyptUtil;
    private final JWTUtil jwtUtil;

    @Transactional
    @Override
    public void sendChat(String token, Long partyId, ChatDto chatDto) {
        System.out.println("chatDto = " + chatDto.toString());
        Long memberId = jwtUtil.getId(token.substring(7 ));

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
//        StringEncryptor encryptor = jasyptUtil.getPartyEncryptor(partyId, chatDto.getPassword());
        StringEncryptor encryptor = jasyptUtil.customEncryptor(chatDto.getPassword());
        System.out.println(chatDto.getPassword());
        String encryptChat = encryptor.encrypt(chatDto.getContent());
        System.out.println("encryptChat = " + encryptChat);
//        System.out.println("채팅 보내기 sseKey : " + chatDto.getPassword());
        // 채팅 만들기
        Chat chat = Chat.customBuilder()
            .chatType(ChatType.CHAT)
            .content(encryptChat)
//            .content(chatDto.getContent())
            .member(member)
            .party(party)
            .build();
        chat.setUserName(profile.getNickname());
        chat.setProfile(profile);

        // 읽은 사람 목록 넣기
        for (ChatMember chatMember : chatMemberList) {
            memberRepository.findById(chatMember.getMemberId())
                .ifPresent(raedMember -> chat.getReadMember().add(raedMember));
        }
        log.info("읽은사람 " + chat.getReadMember());
        chatRepository.save(chat);

        SimpleChatDto simpleChatDto = chatMapper.toSimpleChatDto(chat);
        simpleChatDto.setContent(chatDto.getContent());
        simpleChatDto.setReadNum(memberPartyList.size() - chat.getReadMember().size());

        ResponseEntity<ResultResponse> res = ResponseEntity.ok(
            new ResultResponse(ResultCode.CHAT_SEND_SUCCESS, simpleChatDto));

        template.convertAndSend("/sub/chats/party/" + partyId, simpleChatDto);
    }



    @Override
    public ChatPageDto getChatList(String sseKey, Long partyId, Pageable pageable) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();
        if(memberId.equals(0L)){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }
        // Member Check
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        // Party Check
        partyRepository.findById(partyId).orElseThrow(()-> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        // 유저 목록을 가져옴
        List<MemberParty> memberPartyList = memberpartyRepository.findMemberList(partyId);


        // JasyptCustomEncryptor
//        StringEncryptor encryptor = jasyptUtil.getPartyEncryptor(partyId, sseKey);
        StringEncryptor encryptor = jasyptUtil.customEncryptor(sseKey);
        System.out.println(sseKey);

        Page<Chat> chatPage = chatRepository.findAllByPartyId(partyId, pageable);

        List<SimpleChatDto> chatDtoList = new ArrayList<>(chatPage.stream()
            .map(chat -> {
                String contnet = jasyptUtil.keyDecrypt(encryptor, chat.getContent());

                List<Long> readMembers = chat.getReadMember().stream()
                    .map(Member::getId)
                    .toList();

                if (!readMembers.contains(memberId)) {
                    List<Member> readMemberList = chat.getReadMember();
                    log.info(readMemberList.toString());
                    readMemberList.add(member);
                    chat.setReadMember(readMemberList);
                    log.info(chat.getReadMember().toString());
                    chatRepository.save(chat);
                }
                SimpleChatDto chatDto = chatMapper.toSimpleChatDto(chat);
                chatDto.setContent(contnet);
                chatDto.setReadNum(memberPartyList.size() - chat.getReadMember().size());
                return chatDto;
            })
            .toList());

        Collections.reverse(chatDtoList);

        ChatPageDto res = ChatPageDto.builder()
            .page(pageable.getPageNumber())
            .size(pageable.getPageSize())
            .next(chatPage.hasNext())
            .simpleChatDtos(chatDtoList)
            .build();

        return res;
    }
}
