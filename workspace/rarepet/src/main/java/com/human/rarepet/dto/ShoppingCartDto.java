package com.human.rarepet.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ShoppingCartDto {

	private Date savedListDate;
	private String id;
	private int productNo;
	private int cartQuantity;
	

//	private String sellerId;
//	private String productName;
//	private int price;
//	private String imgUrl;

	private ProductDto product; // nested Dto
	
	
}
