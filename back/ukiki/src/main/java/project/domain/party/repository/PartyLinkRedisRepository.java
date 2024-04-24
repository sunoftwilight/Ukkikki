package project.domain.party.repository;

import org.springframework.data.repository.CrudRepository;
import project.domain.party.redis.PartyLink;

public interface PartyLinkRedisRepository extends CrudRepository<PartyLink, Long> {

}
