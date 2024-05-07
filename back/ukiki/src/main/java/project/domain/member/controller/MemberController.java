package project.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.dto.response.MemberDto;
import project.domain.member.service.MemberService;
import project.global.jwt.JWTUtil;

@Controller
@ResponseBody
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final JWTUtil jwtUtil;

    private final MemberService memberService;

    @PostMapping("/test")
    public ResponseEntity<?> test(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            System.out.println(username);
        }
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
