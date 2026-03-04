package com.human.rarepet.mapper;

import java.util.List;

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
	
	@Update("update shop set verified = true where id = #{id}")
	int verifyShop(String id);
	
	@Update("update member set is_shop = true where id = #{id}")
	int verifyShopMember(String id);
	
}
