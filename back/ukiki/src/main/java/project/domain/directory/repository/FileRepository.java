package project.domain.directory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.domain.directory.collection.File;

public interface FileRepository extends MongoRepository<File, String> {

}
