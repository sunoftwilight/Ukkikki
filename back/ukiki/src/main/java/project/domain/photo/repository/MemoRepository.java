package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.photo.entity.mediatable.Memo;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    Optional<Memo> findByPhotoId(Long photoId);
}
