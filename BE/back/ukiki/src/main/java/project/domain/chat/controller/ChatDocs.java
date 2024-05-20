package project.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.dto.request.ChatPagealbeDto;
import project.domain.chat.dto.response.ChatPageDto;

public interface ChatDocs {

    @Operation(summary = "실시간 채팅 전송하기")
    void sendMessage(String headerToken, Long partyId, ChatDto chatDto);

    @Operation(summary = "채팅 조회하기")
    ResponseEntity<ChatPageDto> getChatList(HttpHeaders headers, Long partyId, Pageable pageable);
}
