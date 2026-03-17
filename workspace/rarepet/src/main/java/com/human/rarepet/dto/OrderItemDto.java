package com.human.rarepet.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private int orderNo;
    private int productNo;
    private int productOrderCount; 
    private int price;
}
