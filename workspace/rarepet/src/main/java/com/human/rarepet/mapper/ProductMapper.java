package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ProductImageDto;
import com.human.rarepet.dto.ShoppingCartDto;

@Mapper
public interface ProductMapper {

	void insertProduct(ProductDto product);
	
	@Insert
	({"<script> "
		+"insert into product_images "
		+ "(product_no, img_url, saved_file_name, original_file_name, sort_order) "
		+ "values "
		+ "<foreach collection='images' item='image' separator=',' nullable='true' > "
		+ "(#{image.productNo}, "
		+ "#{image.imgUrl}, "
		+ "#{image.savedFileName}, "
		+ "#{image.originalFileName}, "
		+ "#{image.sortOrder}) "
		+ "</foreach> "
		+ "</script> "
	})
	void insertProductImages(@Param(value="images") List<ProductImageDto> images);
	
	@Select("select product_no, img_url, saved_file_name, original_file_name, sort_order "
			+ "from product_images where product_no = #{productNo} order by sort_order")
	List<ProductImageDto> selectProductImagesByProductNo(int productNo);
	
//	@Select("select product_no, img_url, saved_file_name, original_file_name "
//			+ "from product_images where product_no = #{productNo} order by sort_order limit 1")
//	ProductImageDto selectProductMainImage(int productNo);
	
	List<ProductDto> showAllProduct();
	List<ProductDto> showAllProductSearch();
	
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
		
		@Result(property = "product", column = "product_no",
				one = @One(select = "com.human.rarepet.mapper.ProductMapper.selectProductByNo"))
	})
	List<ShoppingCartDto> selectShoppingCart(String id);
	
	@Delete("delete from shopping_cart where product_no = #{productNo} and id = #{id}")
	void deleteShoppingCart(@Param("productNo") int productNo, @Param("id") String id);
	
	@Update("update shopping_cart set cart_quantity = #{cartQuantity} where product_no = #{productNo} and id = #{id}")
	void updateShoppingCart(ShoppingCartDto updated);
	
	List<ProductDto> findProductsByFilter(@Param("search") String search, @Param("category") String category);
	
}
