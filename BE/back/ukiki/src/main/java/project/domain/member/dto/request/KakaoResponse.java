package project.domain.member.dto.request;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attribute;
    private final Map<String, Object> profile;

    public KakaoResponse(Map<String, Object> attribute){

        this.attribute = attribute;
        Map<String, Object> kakao_account = (Map<String, Object>) attribute.get("kakao_account");
        this.profile = (Map<String, Object>) kakao_account.get("profile");
    }

    @Override
    public String getProviderId() {
        return "kakao " + attribute.get("id").toString();
    }

    @Override
    public String getUserName() {
        return profile.get("nickname").toString();
    }

    @Override
    public String getProfileUrl() {
        return profile.get("profile_image_url") == null ? null : profile.get("profile_image_url").toString();
    }
}
