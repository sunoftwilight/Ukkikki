package project.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.domain.member.dto.request.CustomOAuth2User;
import project.domain.member.redis.MemberToken;
import project.domain.member.repository.MemberTokenRedisRepository;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    private final MemberTokenRedisRepository memberTokenRedisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userName = customOAuth2User.getName();
        String providerId = customOAuth2User.getProviderId();
        Long id = customOAuth2User.getId();

        // 10분
        String access = jwtUtil.createJWT("access", id, userName, providerId, ((1000L * 60) * 60 * 4));
        // 몇분이더라
        String refresh = jwtUtil.createJWT("refresh", id, userName, providerId, ((1000L * 60 * 60) * 24 * 60));

        // redis token save
        MemberToken memberToken = new MemberToken();
        memberToken.setToken(refresh);
        memberToken.setUserId(id);
        memberTokenRedisRepository.save(memberToken);

//        response.setHeader("access",access);
        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));
        response.sendRedirect("https://k10d202.p.ssafy.io/");
//        response.sendRedirect("http://localhost:3000/");
    }


    public Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 4);
        cookie.setPath("/");
        if(key.equals("refresh")){
            cookie.setHttpOnly(true);
        }
        return cookie;
    }
}
