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
    private String partyId;
    private String key;
}
