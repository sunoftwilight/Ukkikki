package project.domain.article.repository;

import org.springframework.data.repository.CrudRepository;
import project.domain.article.redis.Alarm;

public interface AlarmRedisRepository extends CrudRepository<Alarm, String> {



}
