package project.domain.photo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Face {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long faceId;

    @Column(name = "party_id")
    private Long partyId;

    @Column(name = "face_image_url")
    private String faceImageUrl;

    @Column(name = "origin_image_url")
    private String originImageUrl;

    @Column(name = "encoding", columnDefinition = "LONGTEXT")
    private String encoding;

    @Column(name = "face_Group_number")
    private Integer faceGroupNumber;

    @Column(name = "photo_id")
    private Long photoId;
}
