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
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
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
    public static Likes create(Photo photo, Member member) {
        Likes likes = new Likes();
        likes.setPhoto(photo);
        likes.setMember(member);
        return likes;
    }

    // 생성 관련 영속성 관리
    public void setMember(Member member) {
        this.member = member;
        member.getLikesList().add(this);
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        photo.getLikesList().add(this);
    }

    // 삭제 메서드
    public void delete() {
        this.getMember().getLikesList().remove(this);
        this.getPhoto().getLikesList().remove(this);
        this.photo = null;
        this.member = null;
    }
}
