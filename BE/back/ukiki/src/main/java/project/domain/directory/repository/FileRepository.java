package project.domain.directory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import project.domain.directory.collection.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends MongoRepository<File, String> {

    @Query("{'photoDto._id': ?0}")
    Optional<File> findByPhotoDtoId(Long photoId);

}
