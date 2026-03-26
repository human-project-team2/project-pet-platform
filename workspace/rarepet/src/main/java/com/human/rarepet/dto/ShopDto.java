package com.human.rarepet.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ShopDto extends MemberDto {

	private String id;
	private String shopName;
	private String shopContent;
	private boolean verified;
	private int rating;
	private String businessRegNo;
	private String savedFileName;
	private String originalFileName;
	private String imgUrlShop;
	
}
