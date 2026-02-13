package com.human.rarepet.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ProductDto {
	
	private int productNo;
	private String productName;
	private String shortDescription;
	private String productContent;
	private String productCategory;
	private int productCount;
	private int productPrice;
	private String imgUrl;
	private Date productRegDate;
	private Date productModDate;	
}