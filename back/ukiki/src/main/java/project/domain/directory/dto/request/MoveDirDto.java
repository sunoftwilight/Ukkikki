package project.domain.directory.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveDirDto {
    private String dirId;
    private String toDirId;
}
