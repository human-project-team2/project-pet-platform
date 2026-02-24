package com.human.rarepet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.human.rarepet.dto.MemberDto;

@Mapper
public interface MemberMapper {
	// 회원가입 실행
    void insertMember(MemberDto member);

 // 아이디로 회원 정보 조회
    MemberDto selectMemberById(String id);
    
    // 로그인 할 때,
    MemberDto checkMember(@Param("memberId") String memberId, @Param("passwd") String passwd);

    // 마이페이지 유저 정보 조회 (id를 받아서 MemberDto를 반환)
    MemberDto getUserById(String id);
}
