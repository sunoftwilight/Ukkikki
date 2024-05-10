package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.domain.photo.entity.MetaCode;
import project.domain.photo.entity.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findById(long id);

    @Query("select MAX(p.id), p.photoUrl.thumb_url1 from Photo p join p.metaList m where m.metaCode = :metaCode")
    List<Photo> findByMetaCode(@Param("metaCode") MetaCode metaCode);

//    @Query("select MAX(p.id), p.photoUrl.thumb_url1 from Photo p join p.metaList m where m.metaCode = :metaCode")
//    List<Photo> findByMetaCode(@Param("metaCode") MetaCode metaCode);

}
