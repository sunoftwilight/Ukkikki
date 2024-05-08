package project.domain.directory.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetDirDtov2 {
    private String parentId;
    @Builder.Default
    private List<GetDirInnerDtov2> contentList = new ArrayList<>();
}