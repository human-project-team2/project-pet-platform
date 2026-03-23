package com.human.rarepet.security;

public interface OAuth2Response {
	
	String getProvider();
	String getProviderId();
	String getEmail();
	String getName();

}
