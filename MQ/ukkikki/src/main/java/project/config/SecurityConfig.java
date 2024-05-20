package project.config;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
<<<<<<<< HEAD:BE/back/ukiki/src/main/java/project/global/config/SecurityConfig.java
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import project.domain.member.service.CustomOAuth2UserService;
import project.global.jwt.CustomSuccessHandler;
import project.global.jwt.JWTFilter;
import project.global.jwt.JWTUtil;

import java.util.Arrays;
import java.util.List;
========
>>>>>>>> origin/MQ:MQ/ukkikki/src/main/java/project/config/SecurityConfig.java

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

<<<<<<<< HEAD:BE/back/ukiki/src/main/java/project/global/config/SecurityConfig.java
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    private final JWTUtil jwtUtil;
//    private final CorsFilter corsFilter;


========
>>>>>>>> origin/MQ:MQ/ukkikki/src/main/java/project/config/SecurityConfig.java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http
            .csrf(AbstractHttpConfigurer::disable);
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//        http
//            .addFilter(corsFilter);
        // From 로그인 방식 disable
        http
            .formLogin(AbstractHttpConfigurer::disable);
        // http basic 인증 방식 disable
        http
            .httpBasic(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174", "https://k10d202.p.ssafy.io")); // 모든 출처 허용
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메소드 지정
        configuration.setAllowedHeaders(Arrays.asList("Sec-Websocket-Version","Sec-Websocket-Extensions","Pragma","Host","Cache-Control","Accept-Language","Accept-Encoding","User-Agent","Sec-Ch-Ua-Platform","Sec-Ch-Ua-Mobile","Sec-Ch-Ua","Referer","X-Frame-Options","Sec-WebSocket-Extensions","Sec-WebSocket-Version","Connection","Upgrade","Sec-Websocket-Key","Authorization","text/event-stream" ,"authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "password", "sseKey")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 크레덴셜 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }

}
