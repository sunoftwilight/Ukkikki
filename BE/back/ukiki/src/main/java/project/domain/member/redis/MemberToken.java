package project.domain.member.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "memberToken")
public class MemberToken {

    @Id
    private Long userId;
    private String token;

    @TimeToLive
    @Builder.Default
    private Long ttl = ((1000L * 60 * 60) * 24 * 60);
}
