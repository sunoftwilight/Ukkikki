package project.domain.directory.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import project.domain.directory.collection.Trash;

public interface TrashRepository extends MongoRepository<Trash, String> {
    List<Trash> deleteTrashesByDeadLineIsBefore(LocalDateTime now);

    Optional<Trash> findByRawIdAndDeadLine(String dirOrFileId, LocalDate deadLine);

    @Query(value = "{ 'rawId': ?0 }" )
    Optional<Trash> findFirstByRawId(String rawId);

    List<Trash> findByRawId(String rawId);
}
