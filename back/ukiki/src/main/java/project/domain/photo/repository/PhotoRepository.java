package project.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.domain.party.entity.Party;
import project.domain.photo.entity.MetaCode;
import project.domain.photo.entity.Photo;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findById(long id);

    @Query("select p from Photo p join p.metaList m where p.party = :party and m.metaCode = :metaCode")
    List<Photo> findByMetaCode(@Param("party") Party party, @Param("metaCode") MetaCode metaCode);

}
