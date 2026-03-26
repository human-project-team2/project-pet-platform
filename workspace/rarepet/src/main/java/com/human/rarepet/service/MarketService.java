package com.human.rarepet.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.human.rarepet.exception.AlreadyInCartException;
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
			List<ProductImageDto> images = saveImageFiles(pictures);
			insertProductImages(product.getProductNo(), images);
		} catch (IOException exception) {
			uploaded = false;
			throw new RuntimeException("등록 중 오류가 발생했습니다.", exception);
		}
		return uploaded;
	}

	final String UPLOAD_PATH = "d:\\uploaded_images";

	public List<ProductImageDto> saveImageFiles(List<MultipartFile> pictures) throws IOException {
		List<ProductImageDto> images = new ArrayList<>();
		for (MultipartFile picture : pictures) {
			if (picture.isEmpty()) continue;
			ProductImageDto image = new ProductImageDto();
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			File file = new File(UPLOAD_PATH, savedFileName);
			image.setImgUrl("/images/" + savedFileName);
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

	// MarketService.java 내 searchProducts 메서드 수정

	public List<ProductDto> searchProducts(String search, String category) {
	    String filterCategory = (category == null || "all".equals(category)) ? "" : category;
	    String filterSearch = (search == null) ? "" : search;

	    // 1. DB에서 일단 전체(또는 검색된) 리스트를 가져옵니다.
	    List<ProductDto> allProducts = productMapper.findProductsByFilter(filterSearch, filterCategory);
	    
	    // 2. 홈 화면용 리스트를 새로 만듭니다. (재고가 있는 것만 담기 위해)
	    List<ProductDto> activeProducts = new ArrayList<>();

	    for (ProductDto product : allProducts) {
	        // ✨ 판매 중인 상품(수량 > 0)만 홈 화면 리스트에 추가
	        if (product.getProductCount() > 0) {
	            List<ProductImageDto> images = productMapper.selectProductImagesByProductNo(product.getProductNo());
	            if (!images.isEmpty()) {
	                product.setProductImages(images);
	            }
	            activeProducts.add(product);
	        }
	    }
	    Collections.shuffle(activeProducts);
	    return activeProducts; // 품절 상품이 제외된 리스트만 반환
	}

	public List<ProductDto> showAllProduct() {
		return productMapper.showAllProduct();
	}

	public List<ProductDto> findProductsByUserId(String id) {
		List<ProductDto> myProducts = productMapper.findProductsByUserId(id);
		for (ProductDto product : myProducts) {
			product.setProductImages(productMapper.selectProductImagesByProductNo(product.getProductNo()));
		}
		
		return myProducts;
	}

	public ProductDto selectProductByNo(int productNo) {
		ProductDto product = productMapper.selectProductByNo(productNo);
		product.setProductImages(productMapper.selectProductImagesByProductNo(productNo));
		return product;
	}

	@Transactional
	public void addShoppingCart(int productNo, String id) {
		ProductDto product = productMapper.selectProductByNo(productNo);
		if (product == null) throw new IllegalArgumentException("상품이 존재하지 않습니다.");
		
		ShoppingCartDto added = new ShoppingCartDto();
		added.setSavedListDate(new Date());
		added.setId(id);
		added.setProductNo(productNo);
		added.setCartQuantity(1);
		
		try {
			productMapper.addShoppingCart(added);
		} catch (DuplicateKeyException exception) {
			throw new AlreadyInCartException("이미 장바구니에 담긴 상품입니다.");
		}
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

	@Transactional
	public void updateShoppingCartList(String id, List<UpdateCartQuantityDto> updatedQuantityList) {
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
	public List<ProductDto> getLatestProducts(int page) {
	    int limit = 6; // 한 페이지에 6개
	    int offset = (page - 1) * limit; // 1페이지면 0부터, 2페이지면 6부터 시작
	    
	    List<ProductDto> products = productMapper.selectLatestProducts(limit, offset);
	    List<ProductDto> products2 = new ArrayList<>();
	    for(ProductDto product : products) {
	    	List<ProductImageDto> images = productMapper.selectProductImagesByProductNo(product.getProductNo());
	    	product.setProductImages(images);
	    	products2.add(product);
	    }
	    return products2;
	}
	
}
