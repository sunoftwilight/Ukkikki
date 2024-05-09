package project.domain.member.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.domain.member.dto.response.InfoDto;
import project.domain.member.dto.response.KeyGroupDto;

import java.util.List;

public interface MemberService {

    InfoDto myInfo();
    String reissue(Cookie[] cookies);
    void logout(HttpServletRequest request, HttpServletResponse response);
    void setPassword(String password);
    List<KeyGroupDto> getKeyGroup(String password);

}
