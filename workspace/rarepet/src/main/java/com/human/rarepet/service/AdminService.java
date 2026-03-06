package com.human.rarepet.service;

import java.util.List;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ShopDto;

public interface AdminService {
	
	List<MemberDto> getAllMembers();
	List<ShopDto> getAllShops();
	void verifyShop(String id);
	List<MemberDto> searchMembers(String keyword);
	List<ShopDto> searchShops(String keyword);
}
