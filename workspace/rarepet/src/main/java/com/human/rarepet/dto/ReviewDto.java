package com.human.rarepet.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewDto {

	private int reviewNo;
	private String id;
	private int orderNo;
	private int productNo;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private int rating;
	
	private String productName; 
    
}
