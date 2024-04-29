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
    PARTY_LINK_VALID(200, "그룹 링크가 유효합니다."),
    PASSWORD_CORRECT(200, "비밀번호가 일치합니다"),
    MEMBER_ENTER_SUCCESS(200, "멤버 파티 입장에 성공하였습니다."),
    GUEST_ENTER_SUCCESS(200, "게스트 파티 입장에 성공하였습니다."),
    CHANGE_PASSWORD_SUCCESS(200, "파티 암호 변경에 성공하였습니다."),
    CHANGE_PARTY_NAME_SUCCESS(200, "파티 이름 변경에 성공하였습니다."),
    GRANT_TARGET_SUCCESS(200, "권한 부여에 성공하였습니다."),
    MEMBER_BLOCK_SUCCESS(200, "멤버 차단에 성공하였습니다."),
    MEMBER_KICK_SUCCESS(200, "멤버 추방에 성공하였습니다."),
    ;

    private final int status;
    private final String message;
}
