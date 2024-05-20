package project.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import project.domain.chat.redis.ChatMember;
import project.domain.chat.repository.ChatMemberRedisRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.jwt.JWTUtil;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final ChatMemberRedisRepository chatMemberRedisRepository;
    private final JWTUtil jwtUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/sub"); // 구독
        registry.setApplicationDestinationPrefixes("/pub"); // 메시지 보내기 endpoint 설정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*"); // 허용 URL 패턴
//            .withSockJS(); // SockJS 지원을 활성화합니다.
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                switch (accessor.getCommand()) {
                    case CONNECT:
                        // 헤더에서 멤버 아이디 뽑기
                        String accessHeader = accessor.getFirstNativeHeader("Authorization");
                        String token = accessHeader.substring(7);
                        Long memberId = jwtUtil.getId(token);
                        // 멤버 토큰 넣고 저장
                        chatMemberRedisRepository.save(ChatMember.builder()
                            .memberId(memberId)
                            .sessionId(accessor.getSessionId())
                            .build());
                        break;

                    case SUBSCRIBE:
                        // Destination - sub 주소 저장
                        chatMemberRedisRepository.findBySessionId(accessor.getSessionId())
                            .ifPresent(chatMember -> {
                                chatMember.setDestination(accessor.getDestination());
                                chatMemberRedisRepository.save(chatMember);
                            });
                        break;

                    case DISCONNECT:
                        // 접속 유저 삭제
                        chatMemberRedisRepository.findBySessionId(accessor.getSessionId())
                            .ifPresent(chatMemberRedisRepository::delete);
                        break;
                }

                return message;
            }
        });

    }
}
//class MyChannelInterceptor implements ChannelInterceptor {
//
//    @Override
//    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
//        // 연결이 끊어졌을 때 실행될 코드를 여기에 추가
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//    }
//}