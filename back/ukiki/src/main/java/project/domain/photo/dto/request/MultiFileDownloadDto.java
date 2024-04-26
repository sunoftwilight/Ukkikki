package project.domain.photo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MultiFileDownloadDto {
    @NotNull
    private String key;
    @NotNull
    private List<Long> fileIdList = new ArrayList<>();
    @NotNull
    private String prefix;
}
