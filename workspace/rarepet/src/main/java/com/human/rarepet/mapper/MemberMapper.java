package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.RoleDto;

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
    
    // [추가] 회원 정보 업데이트 실행
    void updateMember(MemberDto member);
    
    @Select("SELECT r.role_no, r.role_name " +
            "FROM member m " +
            "inner JOIN member_role mr ON m.id = mr.id " +
            "inner JOIN role r ON mr.role_no = r.role_no " +
    		"WHERE mr.id = #{id}")
    List<RoleDto> selectMemberRolesById(String id);
    
    @Insert("INSERT INTO member_role (id, role_no) VALUES (#{id}, #{roleNo})")
    void insertMemberRole(@Param("id") String id, @Param("roleNo") int roleNo);
}
