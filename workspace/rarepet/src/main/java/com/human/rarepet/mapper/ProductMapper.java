package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


import com.human.rarepet.dto.ProductDto;

@Mapper
public interface ProductMapper {

	void insertProduct(ProductDto product);
	List<ProductDto> showAllProduct();
}
