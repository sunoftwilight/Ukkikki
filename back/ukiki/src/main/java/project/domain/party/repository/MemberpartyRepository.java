package project.domain.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.Member;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;

import java.util.Optional;

public interface MemberpartyRepository extends JpaRepository<MemberParty, Long> {

    Optional<MemberParty> findByMemberAndParty(Member member, Party party);
}
