package project.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import project.domain.chat.dto.request.ChatDto;

public interface ChatDocs {

    @Operation(summary = "실시간 채팅 전송하기")
    void sendMessage(HttpHeaders headers, Long partyId, ChatDto chatDto);
}
