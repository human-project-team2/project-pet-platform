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
	
}
