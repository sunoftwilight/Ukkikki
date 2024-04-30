package project.domain.directory.collection;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString
@Document(collection = "directory")
public class Directory {

    @Id
    private String id;
    private String dirName;
    private String parentDirId;
    @Builder.Default
    private List<String> childDirIdList = new ArrayList<>();
    @Builder.Default
    private List<Long> photoList = new ArrayList<>();
}
