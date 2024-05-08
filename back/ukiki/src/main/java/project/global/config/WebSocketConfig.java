package project.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
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

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    ChatMemberRedisRepository chatMemberRedisRepository;

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
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication == null) {
                    log.info("WEBSOCKET authentication NULL");
                    return null;
                }
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        Long userId = userDetails.getId();
                // TODO 유저 아이디를 가지고 온다.
                Long memberId = 1L;
                StompHeaderAccessor accessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // String user = accessor.getFirstNativeHeader("userId");
                String destination = accessor.getDestination();


                switch (accessor.getCommand()) {
                    case CONNECT:
                        log.info("TEST CONNECT");
                        break;
                    case SUBSCRIBE:
                        log.info("TEST SUCCESS WEBSOCKET");
                        log.info(destination);
                        chatMemberRedisRepository.save(ChatMember.builder()
                            .memberId(memberId)
                            .destination(destination)
                            .build());
                        break;

                    case DISCONNECT:
                        log.info("TEST DISCONNECT  ", destination);
                        chatMemberRedisRepository.findByMemberId(memberId).ifPresent(chatMember -> {
                            chatMemberRedisRepository.delete(chatMember);
                        });
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