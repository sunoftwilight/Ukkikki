package project.domain.party.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.chat.entity.Chat;
import project.domain.member.entity.Profile;
import project.domain.photo.entity.Photo;
import project.global.baseEntity.BaseEntity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Party extends BaseEntity {

    @Id
    @Column(name = "party_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "party_name")
    private String partyName;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "password")
    private String password;

    @Column(name = "root_dir_id")
    private String rootDirId;

    // 연관관계 총 4개
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Article> arrayList = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Photo> photoList = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Profile> profileList = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<MemberParty> memberPartyList = new ArrayList<>();
}
