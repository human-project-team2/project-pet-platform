package com.human.rarepet.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrderDto {
    private int orderNo;
    private String orderAddr;
    private String orderPhone; // 추가한 연락처
    private String orderDate;
    private String id;
    private int totalPrice;

    // 장바구니 여러 상품을 담을 리스트 (이 이름이 HTML의 orderItems와 일치해야 함)
    private List<OrderItemDto> orderItems; 
}