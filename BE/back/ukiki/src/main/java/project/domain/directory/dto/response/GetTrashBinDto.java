package project.domain.directory.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTrashBinDto {
    private Long id;
    private String trashBinName;
    private List<String> dirIdList = new ArrayList<>();
    private List<String> dirNameList = new ArrayList<>();
    private List<String> fileIdList = new ArrayList<>();
    private List<String> photoUrlList = new ArrayList<>();
}
