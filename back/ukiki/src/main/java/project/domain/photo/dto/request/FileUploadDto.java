package project.domain.photo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
