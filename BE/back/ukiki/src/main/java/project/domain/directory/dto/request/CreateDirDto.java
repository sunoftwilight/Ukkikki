package project.domain.directory.dto.request;

import lombok.Data;

@Data
public class CreateDirDto {
    private String parentDirId;
    private String dirName;
}
