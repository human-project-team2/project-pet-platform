package com.human.rarepet.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.dao.DuplicateKeyException;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ProductImageDto;
import com.human.rarepet.dto.ShoppingCartDto;
import com.human.rarepet.dto.UpdateCartQuantityDto;
import com.human.rarepet.mapper.ProductMapper;

@Service("marketService")
public class MarketService {

    private final ProductMapper productMapper;
    private final SqlSessionFactory sqlSessionFactory;

    public MarketService(SqlSessionFactory sqlSessionFactory, ProductMapper productMapper) {
        this.productMapper = productMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }


    @Transactional
    public boolean registerProduct(ProductDto product, List<MultipartFile> pictures) {
    	
    	boolean uploaded = true;
    	
    	try {
    		productMapper.insertProduct(product);    		
    		List<ProductImageDto> images = saveImageFiles(pictures); // 파일저장.
    		insertProductImages(product.getProductNo(), images); // DB에 정보 저장.
		} catch (IOException exception) {
			// @Transaction 어노테이션에 대해 Runtime Exception과 Error에 대해서만 
			// default로 롤백이 적용되도록 설정되어 있음.
			uploaded = false;
			throw new RuntimeException("등록 중 오류가 발생했습니다.", exception);
			
		}
    	return uploaded;
    }

    final String UPLOAD_PATH = "d:\\uploaded_images";
    // 이미지 여러 개 등록했을 때로 바꿈.
    
    public List<ProductImageDto> saveImageFiles(List<MultipartFile> pictures) throws IOException {
    	
    	List<ProductImageDto> images = new ArrayList<>();
    	
    	for (MultipartFile picture : pictures) {
    		
    		if (picture.isEmpty()) {
    			continue;
    		}
    		
    		ProductImageDto image = new ProductImageDto();
    		String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
    		File file = new File(UPLOAD_PATH, savedFileName);
    		image.setImgUrl("/images/" + savedFileName); // 웹경로
            image.setSavedFileName(savedFileName);
            image.setOriginalFileName(picture.getOriginalFilename());
    		picture.transferTo(file);
    		images.add(image);
    		
    	}
    	
    	return images;
    }
    
	public void insertProductImages(int productNo, List<ProductImageDto> images) {
				
		int order = 1;			
		
		for (ProductImageDto image : images) {
			
			image.setProductNo(productNo);
			image.setSortOrder(order++);
			
        }
		
		productMapper.insertProductImages(images);
		
	}
    
	
    /**
     * 홈 화면 검색 및 카테고리 필터링 로직
     */
    public List<ProductDto> searchProducts(String search, String category) {
        // 카테고리가 null이거나 'all'인 경우 전체 검색을 위해 빈 문자열로 처리
        String filterCategory = (category == null || "all".equals(category)) ? "" : category;
        // 검색어가 null인 경우 빈 문자열 처리
        String filterSearch = (search == null) ? "" : search;
        
        
        List<ProductDto> products = productMapper.findProductsByFilter(filterSearch, filterCategory);
		
        for (ProductDto product : products) {
			
        	List<ProductImageDto> images = productMapper.selectProductImagesByProductNo(product.getProductNo());
			if (!images.isEmpty()) {
				product.setProductImages(images);
			}
		}
        
        return products;
    }
    
	
    
    public List<ProductDto> showAllProduct() {
        return productMapper.showAllProduct();
    }

    // 마이페이지: 특정 유저의 상품 목록 가져오기
    public List<ProductDto> findProductsByUserId(String id) {
    	
        List<ProductDto> myProducts = productMapper.findProductsByUserId(id);
        for(ProductDto product : myProducts) {
        	product.setProductImages(productMapper.selectProductImagesByProductNo(product.getProductNo()));
        }
        return myProducts;
    }

    public ProductDto selectProductByNo(int productNo) {
    	
    	ProductDto product = productMapper.selectProductByNo(productNo);
    	
		product.setProductImages(productMapper.selectProductImagesByProductNo(productNo));
		return product;
    	
    }

    public void addShoppingCart(int productNo, String id) throws DuplicateKeyException {
    	ProductDto product = productMapper.selectProductByNo(productNo);
        if (product == null) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다: " + productNo);
        }
    	ShoppingCartDto added = new ShoppingCartDto();
        added.setSavedListDate(new Date());
        added.setId(id);
        added.setProductNo(productNo);
        added.setCartQuantity(1);

        productMapper.addShoppingCart(added);
    }	
	
    public List<ShoppingCartDto> selectShoppingCart(String id) {
    	
        List<ShoppingCartDto> cartList = productMapper.selectShoppingCart(id);
		for (ShoppingCartDto cart : cartList) {
			ProductDto product = productMapper.selectProductByNo(cart.getProductNo());
			cart.setProduct(product);
		}
        return cartList != null ? cartList : new ArrayList<>();
    }

    public void deleteShoppingCart(int productNo, String id) {
        productMapper.deleteShoppingCart(productNo, id);
    }

    public void updateShoppingCartList(String id, List<UpdateCartQuantityDto> updatedQuantityList) {
        // try-with-resources 방식을 사용하면 close()를 자동으로 호출해줍니다. 
        // 위의 주석은 누가 작성? 자바 자동 기능인듯?
    	try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            
    		ProductMapper batch = session.getMapper(ProductMapper.class);

            for (UpdateCartQuantityDto updated : updatedQuantityList) {
                ShoppingCartDto item = new ShoppingCartDto();
                item.setId(id);
                item.setCartQuantity(updated.getCartQuantity());
                item.setProductNo(updated.getProductNo());
                batch.updateShoppingCart(item);
            }
            session.flushStatements();
            session.commit();
        }
    }
}