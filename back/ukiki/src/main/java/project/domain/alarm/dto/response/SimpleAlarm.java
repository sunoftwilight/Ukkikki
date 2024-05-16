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
    Long contentsId;
    Long targetId;
    String writerNick;
    AlarmType alarmType;
    String content;
    String partyName;
    String partyUrl;
    LocalDateTime createDate;
    List<String> identifier;     // LIST 형태안에 API를 넣어주자
    boolean read;

}
