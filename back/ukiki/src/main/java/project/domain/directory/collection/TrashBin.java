package project.domain.directory.collection;

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
@Document(collection = "trashBin")
public class TrashBin {

    @Id
    // party 와 id 동일
    private Long id;
    private String trashBinName;
    @Builder.Default
    private List<String> dirIdList = new ArrayList<>();
    @Builder.Default
    private List<String> fileIdList = new ArrayList<>();
}
