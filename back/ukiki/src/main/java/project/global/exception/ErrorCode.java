package project.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Global
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 HTTP method입니다."),
    INPUT_VALUE_INVALID(400, "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "입력 타입이 유효하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "request message body가 없거나, 값 타입이 올바르지 않습니다."),
    HTTP_HEADER_INVALID(400, "request header가 유효하지 않습니다."),
    ENTITY_NOT_FOUNT(500, "존재하지 않는 Entity입니다."),
    FORBIDDEN_ERROR(403, "작업을 수행하기 위한 권한이 없습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(404, "해당하는 회원이 존재하지 않습니다."),
    REFRESH_TOKEN_NULL(404, "토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(400, "토큰이 만료되었습니다."),
    REFRESH_TOKEN_MATCH(400, "토큰이 일치하지 않습니다.."),
    MEMBER_LOGOUT_MATCH(400, "access, refresh 토큰 정보가 일치하지 않습니다."),

    // 파티 관련
    PARTY_NAME_INVALID(400, "그룹명 입력이 유효하지 않습니다."),
    PARTY_PASSWORD_INVALID(400, "암호키 입력이 유효하지 않습니다."),
    PARTY_NOT_FOUND(404, "파티가 존재하지 않습니다."),
    PARTY_LINK_INVALID(400, "파티링크가 유효하지 않습니다."),
    INPUT_NUMBER_EXCEED(400, "입력횟수가 초과 되었습니다."),
    NOT_ROLE_MASTER(400, "마스터 권한이 아닙니다."),
    NOT_EXIST_PARTY_USER(404, "존재하지 않는 파티 유저입니다."),
    MASTER_CANT_EXIT(400, "마스터는 파티방을 나갈 수 없습니다."),
    ENTER_DENIED_BLOCK_USER(400, "가입 거부 차단된 유저입니다."),
    PARTY_ALREADY_HAVE_ROOT_DIR(400, "이미 그룹 폴더가 존재 합니다."),
    MEMBER_NOT_PROFILE(400, "프로필을 찾을 수 없습니다."),
    NOT_ROLE_GUEST(400, "게스트에게 없는 권한입니다."),

    // 사진 업로드, 다운로드 관련
    META_CODE_NOT_FOUND(404, "해당 메타코드 분류가 존재하지 않습니다."),
    BASE64_ENCODING_FAIL(400, "Base64 인코딩에 실패했습니다."),
    FILE_NOT_FOUND(404, "해당 파일을 찾을 수 없습니다."),
    FILE_UPLOAD_ERROR(500, "파일 업로드에 실패하였습니다."),
    PHOTO_NOT_FOUND(404, "해당 이미지를 찾을 수 없습니다."),

    // 폴더 관련
    DIRECTORY_NOE_FOUND(404, "해당 폴더를 찾을 수 없습니다."),
    DIRECTORY_OUT_OF_DEADLINE(403, "유효 기간이 만료된 폴더입니다."),
    ROOTDIR_NOT_FOUND(404, "최상위 폴더를 찾을 수 없습니다.(무한 재귀 발생)"),
    FIND_PARENT_OF_ROOT_NOT_AVAILABLE(404, "최상위 폴더의 상위 폴더 입니다."),


    // 휴지통 과련
    TRASHBIN_NOT_FOUND(404, "존재하지 않는 휴지통 입니다."),
    TRASH_CLASSIFICATION_FAIL(422, "분류 불가능한 쓰레기 타입입니다."),

    // 사진 파일 관련
    PHOTO_FILE_NOT_FOUND(404, "해당 사진 파일을 찾을 수 없습니다."),

    // GptUtil 관련
    JSON_PARSE_EXCEPTION(400, "JSON 파싱에 실패했습니다."),

    // Chat
    NOT_JOIN_CHAT(400, "채팅에서 유저를 찾을 수 없습니다.")
    ;

    private final int status;
    private final String message;
}
