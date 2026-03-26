package com.human.rarepet.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FavoriteListDto {

	private String id;
	private int productId;
	private Date favoritedDate;
	
	private String productName;
    private int price;
    private String imgUrl;
}
