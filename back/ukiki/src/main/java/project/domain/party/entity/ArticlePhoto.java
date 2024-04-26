package project.domain.party.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.photo.entity.Photo;
import project.global.baseEntity.BaseEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePhoto extends BaseEntity {

    @Id
    @Column(name = "article_photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계
    @JoinColumn(name = "photo_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Photo photo;

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    // 빌더를 사용한 객체 생성 방법을 재정의 (커스텀 빌더 사용)
    @Builder(builderMethodName = "customBuilder")
    public static ArticlePhoto create(Photo photo, Article article) {
        ArticlePhoto articlePhoto = new ArticlePhoto();
        articlePhoto.setArticle(article);
        articlePhoto.setPhoto(photo);
        return articlePhoto;
    }

    // 생성 관련 영속성 관리
    public void setArticle(Article article) {
        this.article = article;
        if (!article.getArticlePhotoList().contains(this)) {
            article.getArticlePhotoList().add(this);
        }
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        if (!photo.getArticlePhotoList().contains(this)) {
            photo.getArticlePhotoList().add(this);
        }
    }

    // 삭제 메서드
    public void delete() {
        if (this.article != null) {
            this.article.getArticlePhotoList().remove(this);
            this.article = null;
        }
        if (this.photo != null) {
            this.photo.getArticlePhotoList().remove(this);
            this.photo = null;
        }
    }
}
