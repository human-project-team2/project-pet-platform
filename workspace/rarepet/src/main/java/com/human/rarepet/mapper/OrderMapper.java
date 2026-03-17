// OrderMapper.java
package com.human.rarepet.mapper;

import com.human.rarepet.dto.OrderDto;
import com.human.rarepet.dto.OrderItemDto; // 새로 만든 DTO 임포트
import com.human.rarepet.dto.ProductDto;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    // 주문 마스터 저장
    void insertOrderMaster(OrderDto order);

    // [수정] 이제 OrderDto가 아니라 OrderItemDto(한 행씩)를 받습니다.
    void insertOrderDetail(OrderItemDto item);

    // [수정] 재고 차감 (파라미터가 2개이므로 @Param 사용이 안전합니다)
    int decreaseStock(@Param("productNo") int productNo, @Param("quantity") int quantity);
    
    // 장바구니 비우기
    @Delete("delete from shopping_cart where id = #{id} and product_no = #{productNo}")
    void removeShoppingCart(@Param("id") String id, @Param("productNo") int productNo);
    
    // 구매 페이지 출력용
    ProductDto selectProductDetail(int productNo);
}