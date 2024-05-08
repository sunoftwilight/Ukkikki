package project.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import project.domain.member.entity.Member;
import project.domain.member.entity.Profile;
import project.domain.party.entity.Party;
import project.global.baseEntity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Chat extends BaseEntity {

    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ChatType chatType;

    private String userName;


    @Column(name = "content")
    private String content;

    @Builder.Default
    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @JoinColumn(name = "profile")
    @ManyToOne(fetch = FetchType.EAGER)
    private Profile profile;


    @JoinColumn(name = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Party party;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> readMember = new ArrayList<>();

    // 빌더를 사용한 객체 생성 방법을 재정의 (커스텀 빌더 사용)
    @Builder(builderMethodName = "customBuilder")
    public static Chat create(String content, ChatType chatType,Party party, Member member) {
        Chat chat = new Chat();
        chat.chatType = chatType;
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
