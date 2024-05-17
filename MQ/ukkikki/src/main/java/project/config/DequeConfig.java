package project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.dto.MQDto;

import java.util.concurrent.ConcurrentLinkedDeque;

@Configuration
public class DequeConfig {

    @Bean
    public ConcurrentLinkedDeque<MQDto> evenLinkedDeque() {
        return new ConcurrentLinkedDeque<>();
    }
    @Bean
    public ConcurrentLinkedDeque<MQDto> oddLinkedDeque() {
        return new ConcurrentLinkedDeque<>();
    }

}
