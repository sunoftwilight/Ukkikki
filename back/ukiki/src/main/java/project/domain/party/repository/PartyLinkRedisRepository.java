package project.domain.party.repository;

import org.springframework.data.repository.CrudRepository;
import project.domain.party.entity.Party;
import project.domain.party.redis.PartyLink;

import java.util.Optional;

public interface PartyLinkRedisRepository extends CrudRepository<PartyLink, String> {

    Optional<PartyLink> findByParty(Party party);

}
