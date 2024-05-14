package project.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.article.entity.ArticlePhoto;

import java.util.List;

public interface ArticlePhotoRepository extends JpaRepository<ArticlePhoto, Long> {

    List<ArticlePhoto> findAllByPhotoId(Long photoId);
}
