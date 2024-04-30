package project.domain.directory.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirWithChildsNameDto {
    private String id;
    private String dirName;
    private String parentDirId;
    private List<String> childDirIdList = new ArrayList<>();
    private List<String> childDirNameList = new ArrayList<>();
    private List<Long> photoList = new ArrayList<>();
}
