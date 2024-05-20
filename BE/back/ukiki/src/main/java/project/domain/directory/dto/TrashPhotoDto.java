package project.domain.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TrashPhotoDto {

    private Long id;
    private String fileName;
    private int photoNum;
    private String partyId;
    private String memberId;
    private String photoUrl;
    private String thumbUrl1;
    private String thumbUrl2;
}
