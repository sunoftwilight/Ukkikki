package project.domain.photo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class FileDownloadDto {

    @NotNull
    private long fileId;
    @NotNull
    private String prefix;

}
