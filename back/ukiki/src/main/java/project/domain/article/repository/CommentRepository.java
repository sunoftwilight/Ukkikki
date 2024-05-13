package project.domain.article.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.domain.article.collection.CommentCollection;

public interface CommentRepository extends MongoRepository<CommentCollection,Long> {
}
