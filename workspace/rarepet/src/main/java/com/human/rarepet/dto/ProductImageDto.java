package com.human.rarepet.dto;

import lombok.Data;

@Data
public class ProductImageDto {

	int imgNo;
	int productNo;
	String imgUrl;
	String savedFileName;
	String originalFileName;
	int sortOrder;
}
