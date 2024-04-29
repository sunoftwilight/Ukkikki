package project.domain.directory.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenameDirDto {
    private String dirId;
    private String newName;
}
