package project.domain.article.redis;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import project.global.baseEntity.BaseEntity;

import java.time.LocalDateTime;

/**
 * Redis에 저장하는 알람 객체
 * groupId    : 그룹 아이디
 * memberId   : 유저 아이디
 * alarmType  : 알람 타입  PASSWORD , 등등
 * content    : 알람 내용
 * identifier : 식별자 ex> groupId, articleId 등등
 *
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "alarm")
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int alarmId;
    Long partyId;
    Long memberId;
    AlarmType alarmType;
    String content;
    String identifier;

}
