package project.domain.alarm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedirectDto {

    String alarmId;
    String redirectUrl;
}
