package project.domain.member.service;

import jakarta.servlet.http.Cookie;
import project.domain.member.dto.response.InfoDto;

public interface MemberService {
    InfoDto myInfo();
    String reissue(Cookie[] cookies);
}
