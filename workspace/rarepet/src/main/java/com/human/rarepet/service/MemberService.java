package com.human.rarepet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.RoleDto;
import com.human.rarepet.mapper.MemberMapper;
import com.human.rarepet.security.RarepetPasswordEncoder;

@Service("memberService")
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void insertMember(MemberDto member) {
		
		String encoded = passwordEncoder.encode(member.getPasswd());
		member.setPasswd(encoded);
		
		// 3. mybatis 사용
		memberMapper.insertMember(member);
		for (RoleDto role : member.getRoles()) {
			memberMapper.insertMemberRole(member.getId(), role.getRoleNo());
		}
		
	}
	
	public MemberDto checkMember(String memberId, String passwd) {
		
		return memberMapper.checkMember(memberId, passwd);
		
	}
	
	public MemberDto selectMember(String id) {
		return memberMapper.selectMemberById(id);
	}
	// 아이디 중복 확인 로직
    public boolean existsById(String id) {
        // DB에서 해당 ID로 조회했을 때 결과가 null이 아니면 이미 존재하는 것(중복)
        MemberDto member = memberMapper.selectMemberById(id);
        return member != null; 
    }
	

	
	
	// [추가] 마이페이지용 회원 정보 가져오기
    public MemberDto getUserById(String id) {
        return memberMapper.getUserById(id);
    }
    
 // [추가] 회원 정보 업데이트 로직
    public void updateMember(MemberDto member) {
        // 매퍼를 호출해서 DB 값을 바꿉니다.
        memberMapper.updateMember(member);
    }

	public List<RoleDto> selectMemberRolesById(String id) {
		return memberMapper.selectMemberRolesById(id);
	}
	
	
	
	
	
	
}
