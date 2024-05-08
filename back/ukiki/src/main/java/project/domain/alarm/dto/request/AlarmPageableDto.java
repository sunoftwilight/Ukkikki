package project.domain.alarm.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlarmPageableDto {
    int  pageNo;
    int pageSize;
}
