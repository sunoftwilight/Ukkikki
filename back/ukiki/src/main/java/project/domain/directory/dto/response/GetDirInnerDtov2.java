package project.domain.directory.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.domain.directory.collection.DataType;

@Getter
@Setter
@Builder
public class GetDirInnerDtov2 {
    private DataType type;
    private String pk;
    private Long photoId;
    private String name;
    private String url;
    private Boolean isDownload;
    private Boolean isLikes;
}
