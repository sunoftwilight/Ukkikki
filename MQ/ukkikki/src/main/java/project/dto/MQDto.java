package project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class MQDto {
    private MultipartFile file;
    private Long partyId;
    private String key;
    private Long photoId;
}
