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
public class DownloadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "download_log_id")
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
    public static DownloadLog create(Photo photo, Member member) {
        DownloadLog downloadLog = new DownloadLog();
        downloadLog.setPhoto(photo);
        downloadLog.setMember(member);
        return downloadLog;
    }

    // 생성 관련 영속성 관리
    public void setMember(Member member) {
        this.member = member;
        member.getDownloadLogList().add(this);
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        photo.getDownloadLogList().add(this);
    }

    // 삭제 메서드
    public void delete() {
        this.getMember().getDownloadLogList().remove(this);
        this.getPhoto().getDownloadLogList().remove(this);
        this.photo = null;
        this.member = null;
    }
}