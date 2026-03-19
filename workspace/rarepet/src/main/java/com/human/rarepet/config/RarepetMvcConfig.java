package com.human.rarepet.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RarepetMvcConfig implements WebMvcConfigurer {

	@Bean
	JavaMailSender mailSender() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.naver.com");
		mailSender.setPort(465);
		mailSender.setDefaultEncoding("utf-8");
		mailSender.setUsername("bearfootlionpaw");
		mailSender.setPassword("7XVE15YTYPX8");
		
		Properties props = new Properties();
		props.put("mail.debug", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.starttls.required", true);
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.ssl.enable", true);
		props.put("mail.smtp.ssl.trust", true);
		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
}
