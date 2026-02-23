package com.human.rarepet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.human.rarepet.dto.MemberDto;

@Mapper
public interface MemberMapper {
	// 회원가입 실행
    void insertMember(MemberDto member);

    // 아이디 중복 확인 (결과가 1이면 중복, 0이면 사용 가능)
    int checkIdDup(String memberId);
    
    // 로그인 할 때,
    MemberDto checkMember(@Param("memberId") String memberId, @Param("passwd") String passwd);
}
