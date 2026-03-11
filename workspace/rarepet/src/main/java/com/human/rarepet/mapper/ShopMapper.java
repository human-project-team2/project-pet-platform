package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.human.rarepet.dto.ShopDto;

@Mapper
public interface ShopMapper {

	@Insert("insert into shop ( id, shop_content, verified, rating, business_reg_no, shop_name, saved_file_name, original_file_name, img_url_shop) "
			+ "values (#{id}, #{shopContent}, #{verified}, #{rating}, #{businessRegNo}, #{shopName}, #{savedFileName}, #{originalFileName}, #{imgUrlShop})")
	void insertShop(ShopDto shop);
	
	@Select("select shop_content, verified, rating, shop_name, "
			+ "saved_file_name, original_file_name, img_url_shop "
			+ "from shop")
	List<ShopDto> selectAllShops();
	
	@Select("select shop_content, verified, rating, business_reg_no, shop_name, saved_file_name, original_file_name, img_url_shop from "
			+ "shop where id = #{id}")
	ShopDto showShopInfo(String id);
	
	@Update("update shop set shop_content = #{shopContent}, verified = #{verified}, rating = #{rating}, "
			+ "business_reg_no = #{businessRegNo}, shop_name = #{shopName}, saved_file_name = #{savedFileName}, "
			+ "original_file_name = #{originalFileName}, img_url_shop = #{imgUrlShop} where id = #{id}")
	int updateShop(ShopDto shop);
	
}
