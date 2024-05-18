package project.domain.alarm.dto.response;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.domain.alarm.redis.AlarmType;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class SimpleAlarm {

    String alarmId;
    Long partyId;
    String contentsId;
    String targetId;
    String writerNick;
    AlarmType alarmType;
    String content;
    String partyName;
    String partyUrl;
    LocalDateTime createDate;
    String identifier;
    boolean read;

}
