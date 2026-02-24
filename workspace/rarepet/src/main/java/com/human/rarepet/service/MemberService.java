package com.human.rarepet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.mapper.MemberMapper;

@Service("memberService")
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	public void insertMember(MemberDto member)
	{
		// 3. mybatis 사용
		memberMapper.insertMember(member);
		
	}
	
	public MemberDto checkMember(String memberId, String passwd) {
		
		return memberMapper.checkMember(memberId, passwd);
		
	}
	
	// 아이디 중복 확인 로직
    public boolean existsById(String id) {
        // DB에서 해당 ID로 조회했을 때 결과가 null이 아니면 이미 존재하는 것(중복)
        MemberDto member = memberMapper.selectMemberById(id);
        return member != null; 
    }
	
}
