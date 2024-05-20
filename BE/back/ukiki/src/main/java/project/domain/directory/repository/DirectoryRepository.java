package project.domain.directory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.domain.directory.collection.Directory;

public interface DirectoryRepository extends MongoRepository<Directory, String> {

}
