package project.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    //File
    FILE_UPLOAD_SUCCESS(200, "파일이 정상적으로 업로드 되었습니다."),

    ;

    private final int status;
    private final String message;
}
