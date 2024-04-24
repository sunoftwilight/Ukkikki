package project.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    //User
    GET_USERLIST_SUCCESS(200, "유저리스트 검색에 성공하였습니다."),

    //Party
    CREATE_PARTY_SUCCESS(201, "그룹 생성에 성공하였습니다."),
    GET_PARTY_LINK_SUCCESS(201, "참가 링크 생성에 성공하였습니다. "),
    ;

    private final int status;
    private final String message;
}
