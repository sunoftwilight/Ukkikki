package project.domain.photo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadDto {
    @NotNull
    private String key;
    @NotNull
    private long partyId;
    private Double latitude;
    private Double longitude;
    private String rootDirId;
    private String targetDirId;
}
