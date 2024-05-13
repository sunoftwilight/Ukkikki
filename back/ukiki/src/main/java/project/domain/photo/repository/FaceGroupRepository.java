package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.party.entity.Party;
import project.domain.photo.entity.FaceGroup;

import java.util.List;
import java.util.Optional;

public interface FaceGroupRepository extends JpaRepository<FaceGroup, Long> {
    List<FaceGroup> findByPartyId(Long partyId);
    Optional<FaceGroup> findByPartyIdAndFaceGroupNumber(Long partyId, Integer faceGroupNumber);

}
