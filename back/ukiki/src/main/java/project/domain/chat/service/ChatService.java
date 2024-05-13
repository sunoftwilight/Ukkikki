package project.domain.chat.service;

import org.springframework.security.core.userdetails.UserDetails;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.entity.Chat;

public interface ChatService {

    public void sendChat(String token, Long partyId, ChatDto chatDto);
}
