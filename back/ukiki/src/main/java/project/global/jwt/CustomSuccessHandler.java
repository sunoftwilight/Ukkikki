package project.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.domain.member.dto.request.CustomOAuth2User;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userName = customOAuth2User.getName();
        String providerId = customOAuth2User.getProviderId();

        String token = jwtUtil.createJWT(userName, providerId, (long) ((1000 * 60) * 240));

        response.addCookie(createCookies("AccessToken", token));

        response.sendRedirect("http://localhost:3000/");
    }


    public Cookie createCookies(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 4);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
