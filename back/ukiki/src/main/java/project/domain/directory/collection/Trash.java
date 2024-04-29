package project.domain.directory.collection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private String directoryId;
    private LocalDateTime deadLine;
    private String dirName;
    private String parentDirId;
    @Builder.Default
    private List<String> childDirIdList = new ArrayList<>();
    @Builder.Default
    private List<Long> photoList = new ArrayList<>();

}
