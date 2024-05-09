package project.domain.photo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoUrl {

    @Column(name = "photo_url")
    // 원본 사진 url
    private String photoUrl;

    @Column(name = "thumb_url1")
    private String thumb_url1;

    @Column(name = "thumb_url2")
    private String thumb_url2;

    @Column(name = "thumb_url3")
    private String thumb_url3;

    public List<String> photoUrls(){
        List<String> urls = new ArrayList<>(){{
            add(photoUrl);
            add(thumb_url1);
            add(thumb_url2);
            add(thumb_url3);
        }};
        return urls;
    }
}
