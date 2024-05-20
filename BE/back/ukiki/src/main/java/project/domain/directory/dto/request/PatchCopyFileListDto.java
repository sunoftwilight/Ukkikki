package project.domain.directory.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PatchCopyFileListDto {
    private String toDirId;
    private List<String> fileIdList = new ArrayList<>();
}
