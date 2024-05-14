package project.domain.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.dto.request.ChatPagealbeDto;
import project.domain.chat.dto.response.ChatPageDto;
import project.domain.chat.dto.response.SimpleChatDto;
import project.domain.chat.entity.Chat;


import java.util.List;

public interface ChatService {

    public void sendChat(String token, Long partyId, ChatDto chatDto);

    public ChatPageDto getChatList(String sseKey, Long partyId, Pageable pageable);
}
