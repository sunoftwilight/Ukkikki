package project.domain.chat.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


// 사용 목적 몆명이 채팅방에 들어와 있는지 확인
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "chatMember")
public class ChatMember {

    @Id
    Long memberId;

    @Indexed
    String sessionId;

    @Indexed
    String destination; // sub한 주소

    



}
