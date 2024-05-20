package project.domain.directory.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.domain.directory.collection.DataType;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetTrashDto {
    private DataType type;
    private String pk;
    private String name;
    private String url;
    private LocalDate deadLine;
}
