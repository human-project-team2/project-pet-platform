package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ShoppingCartDto;

@Mapper
public interface ProductMapper {

	void insertProduct(ProductDto product);
	
	List<ProductDto> showAllProduct();
	
	List<ProductDto> findProductsByUserId(String id);
	
	@Select("select * from product_all where product_no = #{productNo}")
	ProductDto selectProductByNo(int productNo);
	
	@Insert("insert into shopping_cart values (#{savedListDate}, #{id}, #{productNo}, #{cartQuantity})")
	void addShoppingCart(ShoppingCartDto added);
	
	@Select(
			"select c.saved_list_date, c.cart_quantity, "
			+ "p.seller_id, p.product_no, p.product_name, p.price, p.img_url "
			+ "from shopping_cart c, product_all p where c.product_no = p.product_no and c.id = #{id}")
	@Results({
		@Result(property = "sellerId", column = "seller_id"),
		@Result(property = "productName", column = "product_name"),
		@Result(property = "price", column = "price"),
		@Result(property = "imgUrl", column = "img_url")
		
	})
	List<ShoppingCartDto> selectShoppingCart(String id);
	
}
