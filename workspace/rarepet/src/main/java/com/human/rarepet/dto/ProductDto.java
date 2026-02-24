package com.human.rarepet.dto;


import lombok.Data;

@Data
public class ProductDto {
	
	private int productNo;
	private String productName;
	private String shortDescription;
	private String productContent;
	private String productCategory;
	private int productCount;
	private int price;
	private String imgUrl;
	private java.util.Date productRegDate;
	private java.util.Date productModDate;	
	private String savedFileName;
	private String originalFileName;
	private String id; // DB의 id 컬럼과 매핑될 변수
}