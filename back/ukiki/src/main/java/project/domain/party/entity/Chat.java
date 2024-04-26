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
import org.hibernate.annotations.ColumnDefault;
import project.domain.member.entity.Member;
import project.global.baseEntity.BaseEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseEntity {

    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @JoinColumn(name = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Party party;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 빌더를 사용한 객체 생성 방법을 재정의 (커스텀 빌더 사용)
    @Builder(builderMethodName = "customBuilder")
    public static Chat create(String content, Party party, Member member) {
        Chat chat = new Chat();
        chat.setParty(party);
        chat.setMember(member);
        chat.setContent(content);
        return chat;
    }

    // 생성 관련 영속성 관리
    public void setMember(Member member) {
        this.member = member;
        if (!member.getChatList().contains(this)) {
            member.getChatList().add(this);
        }
    }

    public void setParty(Party party) {
        this.party = party;
        if (!party.getChatList().contains(this)) {
            party.getChatList().add(this);
        }
    }

    // 삭제 메서드
    public void delete() {
        if (this.member != null) {
            this.member.getChatList().remove(this);
        }
        if (this.party != null) {
            this.party.getChatList().remove(this);
        }
        this.isDelete = true;  // 삭제 플래그 설정
        this.party = null;
        this.member = null;
    }
}
