package project.domain.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class TrashFileDto {

    private String id;
    private PhotoDto photoDto;
    private String dirId;
}
