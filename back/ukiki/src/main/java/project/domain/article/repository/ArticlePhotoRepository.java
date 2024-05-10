package project.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.article.entity.ArticlePhoto;

public interface ArticlePhotoRepository extends JpaRepository<ArticlePhoto, Long> {
}
