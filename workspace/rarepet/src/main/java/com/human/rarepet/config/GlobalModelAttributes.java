package com.human.rarepet.config;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.human.rarepet.security.RarepetUserDetails;

@ControllerAdvice
public class GlobalModelAttributes {

	@ModelAttribute("userImgUrl")
	public String userImgUrl(@AuthenticationPrincipal RarepetUserDetails userDetails) {
		
		if (userDetails != null) {
			
			String imgUrl = userDetails.getMember().getImgUrl();
			
			return imgUrl != null ? imgUrl : "/img/avatardefault.jpg";
		}
		return "/ims/avatardefault.jpg";
	}
}
