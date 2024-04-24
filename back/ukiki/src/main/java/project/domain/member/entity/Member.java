package project.domain.member.entity;

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
import project.domain.party.entity.Article;
import project.domain.party.entity.Chat;
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

    @Column(name = "user_name")
    private String userName;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "is_deactivate")
    private boolean isDeactivate = false;

    // 연관관계 총 9 개
    @OneToMany(mappedBy = "member")
    private List<Profile> profileList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Article> articleList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberParty> memberPartyList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Photo> photoList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<DownloadLog> downloadLogList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Memo> memoList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Favorite> favoriteList = new ArrayList<>();
}
