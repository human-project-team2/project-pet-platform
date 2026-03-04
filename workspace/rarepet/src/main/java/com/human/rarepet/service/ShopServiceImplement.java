package com.human.rarepet.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ShopDto;
import com.human.rarepet.mapper.ShopMapper;

import jakarta.servlet.http.HttpSession;

@Service("shopService")
public class ShopServiceImplement implements ShopService {

	private final ShopMapper shopMapper;
	public ShopServiceImplement(ShopMapper shopMapper){
		this.shopMapper = shopMapper;
	}
	
	@Override
	public void insertShop(ShopDto shop, HttpSession session) {
		
		MemberDto member = (MemberDto)session.getAttribute("loginuser");
		shop.setId(member.getId()); 
		shopMapper.insertShop(shop);
	}

	@Override
	public ShopDto showShopInfo(@Param("id") String id) {
		
		return shopMapper.showShopInfo(id) ;
	}

	@Override
	public void updateShop(Map<String, Object> fields) {
		shopMapper.selectiveUpdateShop(fields);
	}

}
