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
public class FaceGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faceGroupId;

    @Column(name = "party_id")
    private Long partyId;

    @Column(name = "face_group_number")
    private Integer faceGroupNumber;

    @Column(name = "encoding", columnDefinition = "LONGTEXT")
    private String encoding;

    @Column(name = "face_list", columnDefinition = "LONGTEXT")
    private String faceList;

    @Column(name = "image_count")
    private Long imageCount;

}
