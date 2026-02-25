package com.human.rarepet.dto;


import java.util.Date;

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
	private Date productRegDate;
	private Date productModDate;	
	private String savedFileName;
	private String originalFileName;
	private String sellerId; 
}