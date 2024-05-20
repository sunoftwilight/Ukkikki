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
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(name = "memo", length = 15)
    private String content;

    @Column(name = "create_date")
    private String date;

    // 연관관계
    @JoinColumn(name = "photo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Photo photo;

    @JoinColumn(name = "member")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 빌더를 사용한 객체 생성 방법을 재정의 (커스텀 빌더 사용)
    @Builder(builderMethodName = "customBuilder")
    public static Memo create(Photo photo, Member member, String content, String date) {
        Memo memo = new Memo();
        memo.setContent(content);
        memo.setPhoto(photo);
        memo.setMember(member);
        memo.setDate(date);
        return memo;
    }

    // 생성 관련 영속성 관리
    public void setMember(Member member) {
        this.member = member;
        member.getMemoList().add(this);
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        photo.getMemoList().add(this);
    }

    // 삭제 메서드
    public void delete() {
        this.getMember().getMemoList().remove(this);
        this.getPhoto().getMemoList().remove(this);
        this.photo = null;
        this.member = null;
        this.content = null;
    }
}
