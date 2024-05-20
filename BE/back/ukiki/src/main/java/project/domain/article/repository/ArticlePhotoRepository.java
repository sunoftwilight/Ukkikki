package project.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.article.entity.ArticlePhoto;

import java.util.List;
import java.util.Optional;

public interface ArticlePhotoRepository extends JpaRepository<ArticlePhoto, Long> {


    Optional<ArticlePhoto> findByArticleIdAndPhotoId(Long articleId, Long photoId);
    List<ArticlePhoto> findAllByPhotoId(Long photoId);
}
