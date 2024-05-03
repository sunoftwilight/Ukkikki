package project.domain.member.dto.request;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.domain.member.dto.response.MemberDto;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberDto memberDto;

    @Override
    public String getName() {
        return memberDto.getUserName();
    }

    public String getProviderId(){
        return memberDto.getProviderId();
    }

    /*
    아래는
    사용하지 않을 예정.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
