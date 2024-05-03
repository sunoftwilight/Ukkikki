package project.domain.directory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.domain.directory.collection.TrashBin;

public interface TrashBinRepository extends MongoRepository<TrashBin, Long> {

}
