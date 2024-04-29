package project.domain.directory.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDirDto {
    private String parentDirId;
    private String dirName;
}
