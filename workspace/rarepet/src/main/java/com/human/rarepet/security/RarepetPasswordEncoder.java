package com.human.rarepet.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.human.rarepet.common.Util;

public class RarepetPasswordEncoder implements PasswordEncoder {

	private static final String ALGORITHM = "SHA-256";
	
	@Override
	public String encode(CharSequence rawPassword) {
		String passwd = rawPassword.toString();
		// return Util.getHashedString(passwd, ALGORITHM);
		return passwd;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		
		//String hashedRawPassword = Util.getHashedString(rawPassword.toString(), ALGORITHM);
		//return encodedPassword.equals(encodedPassword);
		return encodedPassword.equals(rawPassword.toString());
		
	}

}
