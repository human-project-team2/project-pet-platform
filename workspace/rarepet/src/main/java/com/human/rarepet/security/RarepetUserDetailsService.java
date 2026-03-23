package com.human.rarepet.security;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.RoleDto;
import com.human.rarepet.service.MemberService;

public class RarepetUserDetailsService implements UserDetailsService {

	
	private final MemberService memberService;
	
	public RarepetUserDetailsService(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		RarepetUserDetails userDetails = null;
		MemberDto member = memberService.selectMember(username);
		
		if (member != null) {
			List<RoleDto> roles = memberService.selectMemberRolesById(username);
			member.setRoles(roles);
			member.getName();
			userDetails = new RarepetUserDetails(member, member.getName());
			return userDetails;
		} else {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
		}
		
	}

}
