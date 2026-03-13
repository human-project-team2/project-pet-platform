package com.human.rarepet.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private int orderNo;
    private int productNo;
    // DB 컬럼명이 product_order_count이므로 이름을 맞춰줍니다.
    private int productOrderCount; 
    private int price;
}
