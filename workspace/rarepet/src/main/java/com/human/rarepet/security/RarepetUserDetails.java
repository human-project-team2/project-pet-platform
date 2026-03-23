package com.human.rarepet.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.RoleDto;

public class RarepetUserDetails implements UserDetails, OAuth2User {

	private MemberDto member;
	private String name;
	
	public RarepetUserDetails(MemberDto member, String name) {
		this.member = member;
		this.name = name;
	}

	public MemberDto getMember() {
		return member;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<SimpleGrantedAuthority> grants = new ArrayList<>();
		for (RoleDto role : member.getRoles()) {
			grants.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		return grants;
		
	}
	
	@Override
	public String getPassword() {
		
		return member.getPasswd();
	}

	@Override
	public String getUsername() {
		return member.getId();
	}

	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return !member.isNotUser();
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
