package project.domain.directory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.domain.directory.collection.Trash;

public interface TrashRepository extends MongoRepository<Trash, String> {

}
