package project.domain.alarm.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class AlarmPageDto {

    int pageNo;
    int pageSize;
    List<SimpleAlarm> alarmList;
    @Builder.Default
    Boolean last = false;
}
