package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.photo.entity.Face;

import java.util.List;

public interface FaceRepository extends JpaRepository<Face, Long> {
    List<Face> findByOriginImageUrl(String originImageUrl);
}
