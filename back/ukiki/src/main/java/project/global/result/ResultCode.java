package project.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    //User
    GET_USERLIST_SUCCESS(200, "유저리스트 검색에 성공하였습니다."),
    GET_USERINFO_SUCCESS(200, "유정 정보 조회에 성공하였습니다."),
    GET_KEYGROUP_SUCCESS(200, "유저 키 그룹을 가져오는데 성공하였습니다."),

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
    GET_BLOCK_USER_LIST_SUCCESS(200, "차단유저리스트 검색에 성공하였습니다."),
    CHANGE_THUMB_SUCCESS(200, "썸네일 변경에 성공하였습니다."),
    GET_PARTY_LIST_SUCCESS(200, "파티 리스트 조회에 성공하였습니다."),

    //File
    FILE_UPLOAD_SUCCESS(200, "파일이 정상적으로 업로드 되었습니다."),
    FILE_DOWNLOAD_SUCCESS(200, "파일이 정상적으로 다운로드 되었습니다."),
    FILE_DELETE_SUCCESS(200, "파일이 정상적으로 삭제 되었습니다."),
    FILE_COPY_SUCCESS(200, "파일 복사에 성공하였습니다."),
    FILE_MOVE_SUCCESS(200, "파일 이동에 성공하였습니다."),
    GET_FILE_SUCCESS(200, "파일 조회에 성공하였습니다."),


    // Directory
    GET_DIRECTORY_SUCCESS(200, "폴더 조회에 성공하였습니다."),
    CREATE_DIRECTORY_SUCCESS(201, "폴더 생성에 성공하였습니다."),
    MOVE_DIRECTORY_SUCCESS(200, "폴더 이동에 성공하였습니다."),
    RENAME_DIRECTORY_SUCCESS(200, "폴더 이름 수정에 성공하였습니다."),
    DELETE_DIRECTORY_SUCCESS(204, "폴더 삭제에 성공하였습니다."),
    RESTORE_DIRECTORY_OR_FILE_SUCCESS(201, "폴더 또는 파일 복원에 성공하였습니다."),
    GET_DIRECTORYLIST_SUCCESS(200, "해당 유저의 모든 그룹 조회에 성공하였습니다."),
    SET_MAIN_DIRECTORY_SUCCESS(200, "메인 폴더 설정 변경에 성공하였습니다."),
    GET_CHILD_DIR_SUCCESS(200, "하위 폴더 조회에 성공하였습니다."),
    GET_THUMBNAIL_URL_2_SUCCESS(200, "해당 폴더의 모든 썸네일2 사진 조회에 성공하였습니다."),

    // 휴지통 관련
    GET_TRASH_BIN_SUCCESS(200, "휴지통 조회에 성공하였습니다."),

    // alarm
    GET_ALARM_SUCCESS(200, "알람 조회에 성공하였습니다."),

    // Chat
    CHAT_SEND_SUCCESS(200, "채팅 전송에 성공하였습니다."),

    // Article 관련
    CREATE_ARTICLE_SUCCESS(201, "게시판 생성에 성공하였습니다.")
    ;

    private final int status;
    private final String message;
}
