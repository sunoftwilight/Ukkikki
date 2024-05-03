package project.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserName(String username);
}
