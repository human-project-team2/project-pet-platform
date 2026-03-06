package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ShopDto;

@Mapper
public interface AdminMapper {
	
	@Select("SELECT id, name, phone, address, email, is_shop FROM member")
	List<MemberDto> getAllMembers();
	
	@Select("select * from shop")
	List<ShopDto> getAllShops();
	
	// 관리자가 가게 승인 시 shop 테이블의 verified 컬럼을 true로 업데이트, 
	// member 테이블의 is_shop 컬럼을 true로 업데이트, 
	// member_role 테이블의 role_no 컬럼을 3으로 업데이트
	@Update("update shop set verified = true where id = #{id}")
	int verifyShop(String id);
	
	@Update("update member set is_shop = true where id = #{id}")
	int verifyShopMember(String id);
	
	@Insert("insert member_role (id, role_no) values (#{id}, 3)")
	int updateMemberRole(String id);
	
	// 검색 기능
	@Select("SELECT id, name, phone, address, email, is_shop "
			+ "FROM member WHERE id LIKE CONCAT('%', #{keyword}, '%') "
				+ "OR name LIKE CONCAT('%', #{keyword}, '%')")
	List<MemberDto> searchMembers(String keyword);
	
	@Select("SELECT * FROM shop WHERE id LIKE CONCAT('%', #{keyword}, '%') "
            + "OR shop_name LIKE CONCAT('%', #{keyword}, '%')")
	List<ShopDto> searchShops(String keyword);
	
	
}
