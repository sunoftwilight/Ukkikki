package project.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.domain.member.entity.MemberRole;
import project.domain.member.entity.Profile;
import project.domain.party.entity.Party;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByMemberIdAndPartyId(Long memberId, Long PartyId);

    List<Profile> findAllByPartyId(Long partyId);

    @Query("select p from Profile p join MemberParty m on p.member = m.member where m.party = :party and p.party = :party and m.memberRole != :memberRole")
    List<Profile> findAllByPartyIdWithoutBlock(@Param("party") Party party, @Param("memberRole") MemberRole memberRole);
}
