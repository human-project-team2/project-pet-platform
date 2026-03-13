package com.human.rarepet.dto;

import java.util.List;

import lombok.Data;

@Data
public class MemberDto {

	private String id;
	private String passwd;
	private String name;
	private String phone;
	private String address;
	private String email;
	private boolean notUser;

	private boolean isShop;
	private String imgUrl;
	private String savedFileName;
	private String originalFileName;
	
	private List<RoleDto> roles;

	
}
