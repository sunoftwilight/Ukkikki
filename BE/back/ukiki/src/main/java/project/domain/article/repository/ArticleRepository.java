package project.domain.article.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.article.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByPartyId(Long partyId, Pageable pageable);
}
