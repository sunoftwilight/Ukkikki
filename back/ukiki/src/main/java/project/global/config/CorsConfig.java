package project.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("https://jiangxy.github.io/websocket-debug-tool/","https://jiangxy.github.io","http://localhost:5173", "http://localhost:5174", "https://j10d103.p.ssafy.io")
            .allowedOriginPatterns("/api/ws/*, /ws/*", "/sub/*", "/pub/*")
            .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
                HttpMethod.PATCH.name())
            .allowedHeaders("Sec-WebSocket-Extensions","Sec-WebSocket-Version","Connection","Upgrade","Sec-Websocket-Key","authorization","Authorization", "text/event-stream","Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers","X-Frame-Options")
            .allowCredentials(true)
            .exposedHeaders("Authorization")
            .maxAge(3600); // Pre-flight Caching
    }
}

//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter(){
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration configuration = new CorsConfiguration();
//        // 서버 응답 JSON을 JS에서 처리할 수 있게 할 것인가 -> false이면 JS로 요청 시 응답이 오지 않음
//        configuration.setAllowCredentials(true);
//        // 모든 IP에 응답을 허용하겠다
//        configuration.addAllowedOrigin("*");
//        // 모든 헤더에 응답을 허용하겠다
//        configuration.addAllowedHeader("*");
//        // POST, GET, DELETE, PATCH 요청을 허용하겠다
//        configuration.addAllowedMethod("*");
////
////        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
////        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
////        configuration.setAllowedHeaders(List.of("authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
////        configuration.setExposedHeaders(List.of("authorization"));
//
//        source.registerCorsConfiguration("/**", configuration);
//        return new CorsFilter(source);
//    }
//}

