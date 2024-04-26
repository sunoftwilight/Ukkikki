package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.photo.entity.Photo;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findById(long id);

}
