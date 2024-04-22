package project.domain.photo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoUrl {
    @Lob
    @Column(name = "photo_url", columnDefinition = "MEDIUMTEXT")
    // 원본 사진 url
    private String photoUrl;

    @Lob
    @Column(name = "thumb_url1", columnDefinition = "MEDIUMTEXT")
    private String thumb_url1;

    @Lob
    @Column(name = "thumb_url2", columnDefinition = "MEDIUMTEXT")
    private String thumb_url2;

    @Lob
    @Column(name = "thumb_url3", columnDefinition = "MEDIUMTEXT")
    private String thumb_url3;
}
