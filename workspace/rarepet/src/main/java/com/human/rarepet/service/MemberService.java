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
	
	public boolean isIdAvailable(String id) {
		// 매퍼에 만든 int checkIdDup(String memberId)를 호출합니다.
		int count = memberMapper.checkIdDup(id);
		
		// 검색된 개수가 0이면 사용 가능(true), 아니면 중복(false)
		return count == 0;
	}
	
	// [추가] 마이페이지용 회원 정보 가져오기
    public MemberDto getUserById(String id) {
        return memberMapper.getUserById(id);
    }
}
