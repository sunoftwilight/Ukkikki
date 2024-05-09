package project.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.KeyGroup;
import project.domain.member.entity.Member;
import project.domain.party.entity.Party;

import java.util.List;
import java.util.Optional;


public interface KeyGroupRepository extends JpaRepository<KeyGroup, Long> {

    List<KeyGroup> findByMember(Member member);
    Optional<KeyGroup> findByMemberAndParty(Member member, Party party);

}
