package com.human.rarepet.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.RoleDto;

public class RarepetOAuth2User implements OAuth2User {

	private MemberDto member;
	private OAuth2Response oAuth2Response;
	
	public RarepetOAuth2User(OAuth2Response oAuth2Response, MemberDto member) {
		this.oAuth2Response = oAuth2Response;
		this.member = member;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<SimpleGrantedAuthority> grants = new ArrayList<>();
		for (RoleDto role : member.getRoles()) {
			grants.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		return grants;
	}

	@Override
	public String getName() {
		// return oAuth2Response.getName();
		return member.getName();
	}
	
	public String getEmail() {
		// return String.format("%s %s", oAuth2Response.getProvider(), oAuth2Response.getProviderId());
		return member.getEmail();
	}

	
}
