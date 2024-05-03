package project.domain.alarm.dto.response;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.domain.alarm.redis.AlarmType;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class SimpleAlarm {

    Long partyId;
    Long articleId;
    Long targetId;
    AlarmType alarmType;
    String content;
    LocalDateTime createDate;
    List<String> identifier;     // LIST 형태안에 API를 넣어주자
    int read;

}
