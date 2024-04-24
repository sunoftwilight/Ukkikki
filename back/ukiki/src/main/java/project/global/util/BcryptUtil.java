package project.global.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


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
