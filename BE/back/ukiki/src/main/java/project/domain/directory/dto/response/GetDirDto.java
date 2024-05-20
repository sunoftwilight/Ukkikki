package project.domain.directory.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDirDto {
    private String id;
    private String dirName;
    private String parentDirId;
    private String parentDirName;
    private List<String> childDirIdList = new ArrayList<>();
    private List<String> childDirNameList = new ArrayList<>();
    private List<String> fileIdList = new ArrayList<>();
    private List<String> photoUrlList = new ArrayList<>();
}
