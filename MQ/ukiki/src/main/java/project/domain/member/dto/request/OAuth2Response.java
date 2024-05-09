package project.domain.member.dto.request;

public interface OAuth2Response {
    // 제공자 (Ex. naver, kakao, google.....)
    // 제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    // 사용자 실명 (설정한 이름)
    String getUserName();
    // 프로필 이미지
    String getProfileUrl();
}
