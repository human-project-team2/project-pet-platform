package com.human.rarepet.service;

import java.util.List;

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
	public void insertShop(ShopDto shop, String id) {
		
		
		shop.setId(id); 
		shopMapper.insertShop(shop);
	}

	@Override
	public ShopDto showShopInfo(@Param("id") String id) {
		
		return shopMapper.showShopInfo(id) ;
	}

	@Override
	public void updateShop(ShopDto shop) {
		
		shopMapper.updateShop(shop);
	}

	@Override
	public List<ShopDto> selectAllShops() {
		return shopMapper.selectAllShops();
		
	}

}
