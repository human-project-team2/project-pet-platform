package com.human.rarepet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ShoppingCartDto;
import com.human.rarepet.dto.UpdateCartQuantityDto;
import com.human.rarepet.mapper.ProductMapper;

@Service("marketService")
public class MarketService {
	
	
	private final ProductMapper productMapper;
	private final SqlSessionFactory sqlSessionFactory;
	
	public MarketService(SqlSessionFactory sqlSessionFactory,
						 ProductMapper productMapper) {
		this.productMapper = productMapper;
		this.sqlSessionFactory = sqlSessionFactory;
	}

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
    
    public void addShoppingCart(int productNo, String id) throws DuplicateKeyException {
    	
    	ShoppingCartDto added = new ShoppingCartDto();
    	added.setSavedListDate(new Date());
    	added.setId(id);
    	added.setProductNo(productNo);
    	added.setCartQuantity(1);
    	
    	productMapper.addShoppingCart(added);
    	
    }
    
    public List<ShoppingCartDto> selectShoppingCart(String id){
    	List<ShoppingCartDto> cartList = productMapper.selectShoppingCart(id);
    	return cartList != null ? cartList : new ArrayList<>(); 
    }
    
    public void deleteShoppingCart(int productNo, String id) {
    	productMapper.deleteShoppingCart(productNo, id);
    }
    
    public void updateShoppingCartList(String id, List<UpdateCartQuantityDto> updatedQuantityList) {
    	
    	SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
    	try {
 		//좀더 최근 방식?: try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH) {
    		
    		ProductMapper batch = session.getMapper(ProductMapper.class);
    		
    		for(UpdateCartQuantityDto updated : updatedQuantityList) {
    			ShoppingCartDto item = new ShoppingCartDto();
    			item.setId(id);
    			item.setCartQuantity(updated.getCartQuantity());
    			item.setProductNo(updated.getProductNo());
    			batch.updateShoppingCart(item);
    		}
    		session.flushStatements();
    		session.commit();
    		
    	} finally {
    		session.close();
    	}
    }
    
}
