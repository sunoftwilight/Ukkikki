package project.domain.party.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import project.domain.member.entity.MemberRole;
import project.global.baseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberParty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_party_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;

    // 연관관계
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Party party;

    // 빌더를 사용한 객체 생성 방법을 재정의 (커스텀 빌더 사용)
    @Builder(builderMethodName = "customBuilder")
    public static MemberParty create(Party party, Member member, MemberRole memberRole) {
        MemberParty memberParty = new MemberParty();
        memberParty.setMemberRole(memberRole);
        memberParty.setParty(party);
        memberParty.setMember(member);
        return memberParty;
    }

    // 생성 관련 영속성 관리
    public void setMember(Member member) {
        this.member = member;
        member.getMemberPartyList().add(this);
    }

    public void setParty(Party party) {
        this.party = party;
        party.getMemberPartyList().add(this);
    }

    // 삭제 메서드
    public void delete() {
        this.getMember().getMemberPartyList().remove(this);
        this.getParty().getMemberPartyList().remove(this);
        this.party = null;
        this.member = null;
    }

}
