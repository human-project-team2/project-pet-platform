package com.human.rarepet.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.human.rarepet.mapper.AuthMapper;

import jakarta.mail.internet.MimeMessage;

@Service
public class AuthServiceImplement implements AuthService{

	private JavaMailSender javaMailSender;
	private final AuthMapper authMapper;
	
	public AuthServiceImplement(JavaMailSender javaMailSender, AuthMapper authMapper) {
		this.javaMailSender = javaMailSender;
		this.authMapper = authMapper;
	}
	
	@Override
	public int makeCode() {
		
		int mailCode = 0;
		
		mailCode = (int)(Math.random() * 89999999) + 10000000;
		
		return mailCode;
	}

	@Override
	public boolean sendEmail(String email, int mailCode) {
		boolean success = true;
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setFrom("bearfootlionpaw@naver.com");
			messageHelper.setTo(new String[] {email});
			message.setSubject("AnyZoo 인증번호 전송 메일입니다.");
			message.setContent(String.format(
				    "<!DOCTYPE html>" +
				    	    "<html>" +
				    	    "<head>" +
				    	    "  <meta charset='UTF-8'>" +
				    	    "  <style>" +
				    	    "    .container { font-family: Arial, sans-serif; padding: 20px; }" +
				    	    "    .code { font-size: 24px; font-weight: bold; color: #2c3e50; }" +
				    	    "    .footer { margin-top: 20px; font-size: 12px; color: #888; }" +
				    	    "  </style>" +
				    	    "</head>" +
				    	    "<body>" +
				    	    "  <div class='container'>" +
				    	    "    <h2>이메일 인증 안내</h2>" +
				    	    "    <p>안녕하세요! 아래 인증 코드를 입력해 주세요.</p>" +
				    	    "    <p class='code'>%s</p>" +
				    	    "    <p>해당 코드는 일정 시간 후 만료됩니다.</p>" +
				    	    "    <div class='footer'>" +
				    	    "      <p>본 메일은 자동 발송되었습니다.</p>" +
				    	    "    </div>" +
				    	    "  </div>" +
				    	    "</body>" +
				    	    "</html>",
				    	    mailCode
				    	),"text/html;charset=utf-8");
			javaMailSender.send(message);	
		} catch (Exception ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}

	@Override
	public boolean checkDuplicateEmail(String email) {
		
		int no = authMapper.checkDuplicateEmail(email);
		if(no == 0) {
			return false;
		} else {
			return true;
		}
	}

	
}
