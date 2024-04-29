package project.domain.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberRole;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.redis.PartyLink;

import java.util.List;
import java.util.Optional;

public interface MemberpartyRepository extends JpaRepository<MemberParty, Long> {

    Optional<MemberParty> findByMemberAndParty(Member member, Party party);

    Optional<MemberParty> findByMemberAndPartyAndMemberRoleIs(Member member, Party party, MemberRole memberRole);

    Optional<MemberParty> findByMemberIdAndPartyIdAndMemberRoleIs(Long memberId, Long partyId, MemberRole memberRole);

    Optional<MemberParty> findByMemberIdAndPartyId(Long memberid, Long partyId);

    int countAllByPartyIdAndMemberRoleIsNot(Long partyId, MemberRole memberRole);

    List<MemberParty> findAllByParty(Party party);

    @Query("SELECT m FROM MemberParty m " +
        " WHERE m.party.id = :partyId" +
        " AND (m.memberRole = project.domain.member.entity.MemberRole.MASTER " +
        " OR m.memberRole = project.domain.member.entity.MemberRole.EDITOR" +
        " OR m.memberRole = project.domain.member.entity.MemberRole.VIEWER)" +
        " ORDER BY CASE m.memberRole " +
        " WHEN project.domain.member.entity.MemberRole.MASTER THEN 1 " +
        " WHEN project.domain.member.entity.MemberRole.EDITOR THEN 2 " +
        " WHEN project.domain.member.entity.MemberRole.VIEWER THEN 3 " +
        " ELSE 4 END ")
    List<MemberParty> findMemberList(Long partyId);

    List<MemberParty> findAllByPartyIdAndMemberRoleIs(Long partyId, MemberRole memberRole);

}
