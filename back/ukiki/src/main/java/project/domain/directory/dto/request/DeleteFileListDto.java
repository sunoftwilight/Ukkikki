package project.domain.directory.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DeleteFileListDto {

    private String sseKey;
    private List<String> fileIdList = new ArrayList<>();
}
