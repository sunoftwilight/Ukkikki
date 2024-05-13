package project.domain.article.collection;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import project.domain.member.entity.Profile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comment")
public class CommentCollection {

    @Id
    private Long id;
    @Builder.Default
    private List<Comment> comment = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Comment {
        private Long userId;
        private String content;
        private String userName;
        private String profileUrl;
        private String createdDate;
        @Builder.Default
        private Boolean isDelete = false;
        @Builder.Default
        private List<Reply> reply = new ArrayList<>();
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reply {
        private Long userId;
        private String content;
        private String userName;
        private String profileUrl;
        private String createdDate;
        @Builder.Default
        private Boolean isDelete = false;
    }
}
