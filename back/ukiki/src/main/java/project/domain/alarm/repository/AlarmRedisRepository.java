package project.domain.alarm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import project.domain.alarm.redis.Alarm;

public interface AlarmRedisRepository extends CrudRepository<Alarm, String> {

    Page<Alarm> findAllByMemberId(Long memberId, Pageable pageable);
}
