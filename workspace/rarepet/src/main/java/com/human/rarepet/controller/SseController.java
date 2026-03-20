package com.human.rarepet.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.NotificationService;

@RestController //항상 @ResponseBody
@RequestMapping(path = {"/sse"})
public class SseController {
	
	private final NotificationService notificationService;
	
	public SseController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	@GetMapping(path = {"/connect"})
	public SseEmitter connect(@AuthenticationPrincipal RarepetUserDetails userDetails ) {
		
		String id = userDetails.getMember().getId();
		return notificationService.connect(id);
		
	}
	
}
