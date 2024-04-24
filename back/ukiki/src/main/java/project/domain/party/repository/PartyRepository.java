package project.domain.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.party.entity.Party;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
