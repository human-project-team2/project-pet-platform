package com.human.rarepet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ShoppingCartDto;
import com.human.rarepet.mapper.ProductMapper;

@Service("marketService")
public class MarketService {
	
	@Autowired
	private ProductMapper productMapper;

	public void registerProduct(ProductDto product) {
		
		product.setProductRegDate(new Date());
		productMapper.insertProduct(product);
	}
	
	public List<ProductDto> showAllProduct(){
		return productMapper.showAllProduct();
	}
	
	// 마이페이지: 특정 유저의 상품 목록 가져오기
    public List<ProductDto> findProductsByUserId(String id) {
        return productMapper.findProductsByUserId(id);
    }
    
    public ProductDto selectProductByNo(int productNo) {
    	return productMapper.selectProductByNo(productNo);
    }
    
    public void addShoppingCart(int productNo, String id) {
    	
    	ShoppingCartDto added = new ShoppingCartDto();
    	added.setSavedListDate(new Date());
    	added.setId(id);
    	added.setProductNo(productNo);
    	    	
    	productMapper.addShoppingCart(added);
    }
    
    public List<ShoppingCartDto> selectShoppingCart(String id){
    	List<ShoppingCartDto> cartList = productMapper.selectShoppingCart(id);
    	return cartList != null ? cartList : new ArrayList<>(); 
    }
}
