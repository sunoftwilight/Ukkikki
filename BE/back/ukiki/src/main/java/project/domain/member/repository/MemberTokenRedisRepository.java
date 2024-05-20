package project.domain.member.repository;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import project.domain.member.redis.MemberToken;


@EnableRedisRepositories
public interface MemberTokenRedisRepository extends CrudRepository<MemberToken, Long> {
}
