package com.human.rarepet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ShopDto;
import com.human.rarepet.mapper.AdminMapper;

@Service
public class AdminServiceImplement implements AdminService {

	private final AdminMapper adminMapper;

	public AdminServiceImplement(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}
	
	@Override
	public List<MemberDto> getAllMembers() {
		return adminMapper.getAllMembers();
	}

	@Override
	public List<ShopDto> getAllShops() {
		return adminMapper.getAllShops();
	}

	@Override
	public int verifyShop(String id) {
		adminMapper.verifyShop(id);
		return 0;
	}

}
