package com.human.rarepet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public int verifyShop(String id) {
		adminMapper.verifyShop(id);
		adminMapper.verifyShopMember(id);
		adminMapper.updateMemberRole(id);
		return 0;
	}

	@Override
	public List<MemberDto> searchMembers(String keyword) {
		
		return adminMapper.searchMembers(keyword);
	}

	@Override
	public List<ShopDto> searchShops(String keyword) {
		
		return adminMapper.searchShops(keyword);
	}

}
