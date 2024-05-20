package project.domain.alarm.redis;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "alarm")
public class Alarm {

    @Id
    @Builder.Default
    String alarmId = String.valueOf(UUID.randomUUID());

    Long partyId;
    String contentsId;
    String targetId;
    @Indexed
    Long memberId;
    String writerNick;
    String partyName;
    String partyUrl;
    AlarmType alarmType;
    String content;
    String createDate;
    String identifier;
    @Builder.Default
    Boolean isRead = false;

    @TimeToLive
    @Builder.Default
    private long ttl = 3600 * 7 * 4; //4W

    public Alarm(Alarm alarm, Long memberId) {
        this.partyId = alarm.getPartyId();
        this.contentsId = alarm.getContentsId();
        this.targetId = alarm.getTargetId();
        this.memberId = memberId;
        this.partyUrl = alarm.getPartyUrl();
        this.createDate = alarm.createDate;
        this.partyName = alarm.getPartyName();
        this.writerNick = alarm.getWriterNick();
        this.alarmType = alarm.getAlarmType();
        this.content = alarm.getContent();
        this.identifier = alarm.getIdentifier();
    }

}
