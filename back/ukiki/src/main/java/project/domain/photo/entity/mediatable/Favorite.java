package project.domain.photo.entity.mediatable;

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
import project.domain.member.entity.Member;
import project.domain.photo.entity.Photo;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    // 연관관계
    @JoinColumn(name = "photo_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Photo photo;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 빌더를 사용한 객체 생성 방법을 재정의 (커스텀 빌더 사용)
    @Builder(builderMethodName = "customBuilder")
    public static Favorite create(Photo photo, Member member) {
        Favorite favorite = new Favorite();
        favorite.setPhoto(photo);
        favorite.setMember(member);
        return favorite;
    }

    // 생성 관련 영속성 관리
    public void setMember(Member member) {
        this.member = member;
        member.getFavoriteList().add(this);
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        photo.getFavoriteList().add(this);
    }

    // 삭제 메서드
    public void delete() {
        this.getMember().getFavoriteList().remove(this);
        this.getPhoto().getFavoriteList().remove(this);
        this.photo = null;
        this.member = null;
    }
}