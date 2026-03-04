package com.human.rarepet.service;

import java.util.Map;

import com.human.rarepet.dto.ShopDto;

import jakarta.servlet.http.HttpSession;

public interface ShopService {
	
	void insertShop(ShopDto shop, HttpSession session);
	ShopDto showShopInfo(String id);
	void selectiveUpdateShop(ShopDto shop, HttpSession session);
}
