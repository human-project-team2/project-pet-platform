package com.human.rarepet.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

	SseEmitter connect(String id);
    void broadcast(String message);
    void sendToUser(String id, String message);
	
}
