package com.human.rarepet.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.RoleDto;
import com.human.rarepet.mapper.MemberMapper;

import lombok.Setter;

public class RarepetOAuth2UserService extends DefaultOAuth2UserService {

	private MemberMapper memberMapper;
	
	public RarepetOAuth2UserService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
        // 네이버, 구글, 깃허브 등등 어떤 어떤 인증 값인지 구별하기 위해 인증 제공자를 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        // 각 사이트마다 인증 데이터 규격이 다르기 때문에 OAuth2Response 를 만들어 구별
        // 인증 제공자에 따라 OAuth2Response를 생성
        OAuth2Response oAuth2Response = null;
        
        // 그 값이 네이버면
        if (registrationId.equals("naver")) {
        	System.out.println("---------------> Naver Login");
            oAuth2Response = new NaverOAuth2Response(oAuth2User.getAttributes());
      	// 그 값이 카카오면
//        } else if (registrationId.equals("kakao")) {
//        	System.out.println("----------------> Kakao Login");
//            oAuth2Response = new KakaoOAuth2Response(oAuth2User.getAttributes());
        // 둘다 아니면
        } else {
            System.out.println("로그인 실패");
            throw new IllegalArgumentException("지원하지 않는 로그인 제공자입니다.");
        }

        // 데이터 베이스 저장 관련 구현        
        String provider = oAuth2Response.getProvider();
        String providerId = oAuth2Response.getProviderId();
        String email = oAuth2Response.getEmail();
        String name = oAuth2Response.getName();
        String id = oAuth2Response.getId();
        String imgUrl = oAuth2Response.getProfileImage();
        
        MemberDto member = memberMapper.selectMemberById(id);
        
        if (member == null) {
        	member = new MemberDto();
        	member.setEmail(email);
        	member.setPasswd("sns password");
        	member.setName(name);
        	member.setId(id);
        	member.setImgUrl(imgUrl);
        	
        	List<RoleDto> roles = new ArrayList<RoleDto>();
    		RoleDto role = new RoleDto();
    		role.setRoleNo(2);
			role.setRoleName("member");
    		roles.add(role);
    		member.setRoles(roles);
    		
    		//2. 데이터 처리 ( 서비스 객체에 요청 )
    		memberMapper.insertMember(member);
    		
    		List<RoleDto> roles2 = member.getRoles();
    		for (RoleDto role2 : roles2) {
    			memberMapper.insertMemberRole(member.getId(), role2.getRoleNo());
    		}
    		
		} else {
			List<RoleDto> roles = memberMapper.selectMemberRolesById(id);
			member.setRoles(roles);
		}
        
		
		RarepetOAuth2User userDetails = new RarepetOAuth2User(oAuth2Response, member);
		return new RarepetUserDetails(member,name);
	}
}
