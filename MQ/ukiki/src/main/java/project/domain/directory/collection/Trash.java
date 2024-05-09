package project.domain.directory.collection;

import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString
@Document(collection = "trash")
public class Trash {

    @Id
    private String id;
    private String rawId;
    @Enumerated
    private DataType dataType;
    private Object content;
    private LocalDate deadLine;
}
