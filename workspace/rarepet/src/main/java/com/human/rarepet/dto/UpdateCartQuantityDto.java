package com.human.rarepet.dto;

import lombok.Data;

@Data
public class UpdateCartQuantityDto {
	
	private int productNo;
	private int cartQuantity;
}
