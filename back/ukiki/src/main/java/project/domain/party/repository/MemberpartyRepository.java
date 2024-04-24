package project.domain.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.MemberParty;

public interface MemberpartyRepository extends JpaRepository<MemberParty, Long> {
}
