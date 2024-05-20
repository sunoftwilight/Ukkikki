package project.domain.alarm.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.domain.alarm.dto.response.SimpleAlarm;
import project.domain.alarm.redis.Alarm;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlarmMapper {

    SimpleAlarm toSimpleAlarm(Alarm alarm);

}
