package project.domain.photo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDownloadDto {
    @NotNull
    private String key;
    @NotNull
    private long fileId;
    @NotNull
    private String prefix;
}
