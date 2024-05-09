package project.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.member.entity.Profile;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByMemberIdAndPartyId(Long memberId, Long PartyId);
}
