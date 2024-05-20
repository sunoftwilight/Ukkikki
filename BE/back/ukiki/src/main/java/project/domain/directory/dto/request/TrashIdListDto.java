package project.domain.directory.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TrashIdListDto {
    private String sseKey;
    private List<String> trashIdList = new ArrayList<>();
}
