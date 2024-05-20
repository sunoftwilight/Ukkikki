package project.domain.party.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.domain.party.entity.Party;

@EnableJpaRepositories
public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findPartyByRootDirId(String rootDirId);


//    @Query("SELECT p FROM Party p " +
//        "JOIN MemberParty m " +
//        "ON p.id = m.party.id " +
//        "WHERE m.memberRole != project.domain.member.entity.MemberRole.BLOCK " +
//        "AND m.member.id = :memberId")
    @Query("SELECT p FROM Party p " +
        "JOIN p.memberPartyList m " +
        "WHERE m.memberRole != project.domain.member.entity.MemberRole.BLOCK " +
        "AND m.member.id = :memberId")
    List<Party> findPartyListByMemberId(Long memberId);
}
