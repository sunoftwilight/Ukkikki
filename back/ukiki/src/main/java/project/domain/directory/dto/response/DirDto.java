package project.domain.directory.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirDto {
    private String id;
    private String dirName;
    private String parentDirId;
    private List<String> childDirIdList = new ArrayList<>();
    private List<Long> photoList = new ArrayList<>();
}
