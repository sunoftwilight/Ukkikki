package project.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    //User
    GET_USERLIST_SUCCESS(200, "유저리스트 검색에 성공하였습니다.")

    ;

    private final int status;
    private final String message;
}
