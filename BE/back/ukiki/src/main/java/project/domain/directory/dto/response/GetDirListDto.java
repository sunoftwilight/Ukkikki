package project.domain.directory.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetDirListDto {
    private String pk;
    private String name;
    private String thumbnail;
    private LocalDate createDate;
    private Integer fileNum;
    private Boolean isStar;
}
