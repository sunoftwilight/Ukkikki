package project.domain.chat.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.entity.Chat;
import project.domain.chat.service.ChatService;



@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController implements ChatDocs{

    private final ChatService chatService;

    @Override
    @ResponseBody
    @MessageMapping("/message/{partyId}")
    public void sendMessage(@DestinationVariable("partyId") Long partyId, @Payload ChatDto chatDto) {
        log.info("SEND TEST");
        chatService.sendChat(partyId, chatDto);
    }
}
