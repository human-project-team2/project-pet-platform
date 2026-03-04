package com.human.rarepet.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.ShopDto;

@Mapper
public interface ShopMapper {

	@Insert("insert into shop ( id, shop_content, verified, rating, business_reg_no, shop_name, saved_file_name, original_file_name, img_url_shop) "
			+ "values (#{id}, #{shopContent}, #{verified}, #{rating}, #{businessRegNo}, #{shopName}, #{savedFileName}, #{originalFileName}, #{imgUrlShop})")
	void insertShop(ShopDto shop);
	
	@Select("select shop_content, shop_content, verified, rating, business_reg_no, shop_name, saved_file_name, original_file_name, img_url_shop from "
			+ "shop where id = #{id}")
	ShopDto showShopInfo(String id);
	
	@UpdateProvider(type = Util.class, method = "selectiveUpdateShop")
	String selectiveUpdateShop(Map<String, Object> fields);
	
}
