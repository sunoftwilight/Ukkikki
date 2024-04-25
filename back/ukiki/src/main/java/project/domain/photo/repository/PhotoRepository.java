package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.photo.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Photo findById(long id);
}
