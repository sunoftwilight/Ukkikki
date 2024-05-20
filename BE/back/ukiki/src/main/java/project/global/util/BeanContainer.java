package project.global.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import org.springframework.web.reactive.socket.client.TomcatWebSocketClient;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;

import org.springframework.web.reactive.socket.server.upgrade.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.client.WebSocketClient;


public class BeanContainer {
//    @Bean
//    @Primary
//    WebSocketClient tomcatWebSocketClient() {
//        return new TomcatWebSocketClient();
//    }
    @Bean
    @Primary
    public RequestUpgradeStrategy requestUpgradeStrategy() {
        return new TomcatRequestUpgradeStrategy();
    }
}
