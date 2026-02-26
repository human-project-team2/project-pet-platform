package com.human.rarepet.dto;

import lombok.Data;

@Data
public class ReviewDto {

	private int review_no;
	private String product_name;
	private String reviewer_nick;
	private String reported;
	private String id;
	private String product_no;
}
