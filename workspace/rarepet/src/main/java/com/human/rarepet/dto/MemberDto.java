package com.human.rarepet.dto;

import lombok.Data;

@Data
public class MemberDto {

	private String id;
	private String passwd;
	private String name;
	private String phone;
	private String address;
	private String email;
	private String notUser;

	private boolean isShop;
	

	
}
