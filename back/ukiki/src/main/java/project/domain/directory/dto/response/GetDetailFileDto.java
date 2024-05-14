package project.domain.directory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.domain.article.dto.response.ArticleDirDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detailed file data")
public class GetDetailFileDto {
    private String url;
    private Boolean isDownload;
    private Boolean isLikes;
    private List<ArticleDirDto> articleList;
}
