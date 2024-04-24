package project.global.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


/**
 *   encodeBcrypt : 암호화를 진행하는 함수
 *   
 *   matchesBcrypt : 문자와 암호화된 문자가 일치하는지 확인하는 함수
 */
@Component
public class BcryptUtil {

    
    public String encodeBcrypt(String planeText) {
        return new BCryptPasswordEncoder().encode(planeText);
    }
    public boolean matchesBcrypt(String planeText, String hashValue) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(planeText, hashValue);
    }
}
