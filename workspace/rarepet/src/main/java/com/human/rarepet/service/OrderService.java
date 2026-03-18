package com.human.rarepet.service;

import com.human.rarepet.dto.OrderDto;
import com.human.rarepet.dto.OrderItemDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.exception.ZeroProductException;
import com.human.rarepet.mapper.OrderMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    // 상품 정보 가져오기 (구매 페이지 출력용)
    public ProductDto getProductDetail(int productNo) {
        return orderMapper.selectProductDetail(productNo);
    }

    @Transactional
    public int executeOrder(OrderDto order) {
        
    
    	// 1. 마스터 저장
        orderMapper.insertOrderMaster(order);
        
        String id = order.getId();
        
        // 2. 상세 내역 및 재고 처리
        for(OrderItemDto item : order.getOrderItems()) {
            item.setOrderNo(order.getOrderNo());
            
            int no = orderMapper.decreaseStock(item.getProductNo(), item.getProductOrderCount());
            if (no == 0) {
            	throw new ZeroProductException("재고가 부족합니다.");
            }
            System.out.println(item);
            orderMapper.insertOrderDetail(item);
        // [추가] 3. 구매 완료 후 해당 사용자의 장바구니 비우기
        orderMapper.removeShoppingCart(id, item.getProductNo());
        
        }
        return order.getOrderNo();
    }
    
    public List<OrderDto> selectAllOrderById(String id){
    	List<OrderDto> orders = orderMapper.selectAllOrderById(id);
    	
    	return orders;
    }
    
    public OrderDto selectOrderByOrderNo(int orderNo){
    	return orderMapper.selectOrderByOrderNo(orderNo);
    }
}
    