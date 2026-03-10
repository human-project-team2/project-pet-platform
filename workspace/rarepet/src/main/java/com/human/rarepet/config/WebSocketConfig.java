package com.human.rarepet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
		//.setAllowedOrigins("*")
		.setAllowedOriginPatterns("*") // 모든 출처 허용
		.withSockJS(); 
		// 클라이언트가 WebSocket 연결을 시도할 때 사용할 엔드포인트 설정, 
		// SockJS는 WebSocket을 지원하지 않는 브라우저에서도 사용할 수 있도록 해줌
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic"); // 클라이언트로 메시지를 보낼 때 사용할 prefix
		registry.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 메시지를 보낼 때 사용할 prefix
	}

}
