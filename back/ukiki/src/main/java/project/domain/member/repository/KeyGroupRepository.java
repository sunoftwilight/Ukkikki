package project.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.KeyGroup;
import project.domain.member.entity.Member;

import java.util.List;


public interface KeyGroupRepository extends JpaRepository<KeyGroup, Long> {

    List<KeyGroup> findByMember(Member member);

}
