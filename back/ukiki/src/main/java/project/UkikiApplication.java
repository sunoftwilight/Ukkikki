package project;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UkikiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UkikiApplication.class, args);
    }

    // Spring Data JPA의 등록자, 수정자 생성자 관리 클래스 빈 주입
    // 사용하는 툴에따라 커스텀해서 적용해야한다.
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
