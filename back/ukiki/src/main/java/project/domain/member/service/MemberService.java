package project.domain.member.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.domain.member.dto.response.InfoDto;

public interface MemberService {
    InfoDto myInfo();
    String reissue(Cookie[] cookies);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
