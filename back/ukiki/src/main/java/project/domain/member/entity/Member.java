package project.domain.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.chat.entity.Chat;
import project.domain.party.entity.Article;
import project.domain.party.entity.MemberParty;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.mediatable.DownloadLog;
import project.domain.photo.entity.mediatable.Favorite;
import project.domain.photo.entity.mediatable.Likes;
import project.domain.photo.entity.mediatable.Memo;
import project.global.baseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "is_deactivate")
    @Builder.Default
    private boolean isDeactivate = false;

    @Column(name = "upload_group_id")
    private String uploadGroupId;

    // 연관관계 총 9 개

//    @Builder.Default
//    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Member> readMember = new ArrayList<>();

    // 연관관계 총 9 + 1 개
    @JoinColumn(name = "chat")
    @ManyToOne(fetch = FetchType.LAZY)
    Chat chat;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Profile> profileList = new ArrayList<>();

    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Article> articleList = new ArrayList<>();

    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MemberParty> memberPartyList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Photo> photoList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DownloadLog> downloadLogList = new ArrayList<>();

    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Memo> memoList = new ArrayList<>();

    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Favorite> favoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<KeyGroup> keyGroupList = new ArrayList<>();
}
