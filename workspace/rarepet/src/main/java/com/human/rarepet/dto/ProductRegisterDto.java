package com.human.rarepet.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class ProductRegisterDto {
    // DB 테이블 컬럼 참고 (snake_case -> camelCase)
	private int productNo;
    private String title;            // 상품명 / 품종 (DB: product_name 대응 가능)
    private String category;         // 카테고리 (DB: product_category)
    private int price;           // 분양 가격 (DB: product_price)
    private String content;          // 상세 설명 (DB: product_content)
    private int productCount; // 수량
    private Date productRegDate;
    private Date productModDate;
    
    
    // 파일 수신용 (DB에는 파일명이 저장되겠지만, 수신은 MultipartFile로 함)
    private List<MultipartFile> petImages; 
}
