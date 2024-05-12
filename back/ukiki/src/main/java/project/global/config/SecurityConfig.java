package project.global.config;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
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

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    private final JWTUtil jwtUtil;
//    private final CorsFilter corsFilter;


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
        // oauth2
        http
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService)))
                .successHandler(customSuccessHandler));
        // JWT 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/oauth2/**","/member/reissue","/swagger-ui/**" ,"/**").permitAll()
                        .anyRequest().authenticated());


        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174", "https://j10d103.p.ssafy.io")); // 모든 출처 허용
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메소드 지정
        configuration.setAllowedHeaders(Arrays.asList("X-Frame-Options","Sec-WebSocket-Extensions","Sec-WebSocket-Version","Connection","Upgrade","Sec-Websocket-Key","Authorization","text/event-stream" ,"authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "password")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 크레덴셜 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }

}
