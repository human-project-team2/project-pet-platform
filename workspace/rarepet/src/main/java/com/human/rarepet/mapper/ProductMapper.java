package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.human.rarepet.dto.ProductDto;

@Mapper
public interface ProductMapper {

	void insertProduct(ProductDto product);
	
	List<ProductDto> showAllProduct();
	
	List<ProductDto> findProductsByUserId(String id);
	
	@Select("select * from product_all where product_no = #{productNo}")
	ProductDto selectProductByNo(int productNo);
}
