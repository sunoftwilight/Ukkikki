package project.domain.chat.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.dto.request.ChatPagealbeDto;
import project.domain.chat.dto.response.ChatPageDto;
import project.domain.chat.entity.Chat;
import project.domain.chat.service.ChatService;
import project.domain.member.dto.response.KeyGroupDto;
import project.global.result.ResultResponse;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController implements ChatDocs{

    private final ChatService chatService;

    @Override
    @ResponseBody
    @MessageMapping("/message/{partyId}")
    public void sendMessage(@Header("Authorization") String headerToken, @DestinationVariable("partyId") Long partyId, @Payload ChatDto chatDto) {
        chatService.sendChat(headerToken, partyId, chatDto);
    }

    @Override
    @GetMapping("/chat-list/{partyId}")
    public ResponseEntity<ChatPageDto> getChatList(@RequestHeader HttpHeaders headers, @PathVariable(name = "partyId")Long partyId, @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable){
        ChatPageDto res = chatService.getChatList(headers.getFirst("password"), partyId, pageable);
        return ResponseEntity.ok(res);
    }
}
