package com.human.rarepet.service;

public interface AuthService {

	int makeCode();
	boolean sendEmail(String email, int mailCode);
	boolean checkDuplicateEmail(String email);
}
