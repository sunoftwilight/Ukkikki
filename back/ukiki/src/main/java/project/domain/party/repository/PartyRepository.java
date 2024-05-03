package project.domain.party.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.domain.party.entity.Party;

@EnableJpaRepositories
public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findPartyByRootDirId(String rootDirId);
}
