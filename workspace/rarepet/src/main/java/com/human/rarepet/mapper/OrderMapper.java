// OrderMapper.java
package com.human.rarepet.mapper;

import com.human.rarepet.dto.OrderDto;
import com.human.rarepet.dto.OrderItemDto; // 새로 만든 DTO 임포트
import com.human.rarepet.dto.ProductDto;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Many;

@Mapper
public interface OrderMapper {
    // 주문 마스터 저장
    void insertOrderMaster(OrderDto order);

    // [수정] 이제 OrderDto가 아니라 OrderItemDto(한 행씩)를 받습니다.
    void insertOrderDetail(OrderItemDto item);

    // [수정] 재고 차감 (파라미터가 2개이므로 @Param 사용이 안전합니다)
    int decreaseStock(@Param("productNo") int productNo, @Param("productOrderCount") int productOrderCount);
    
    // 장바구니 비우기
    @Delete("delete from shopping_cart where id = #{id} and product_no = #{productNo}")
    void removeShoppingCart(@Param("id") String id, @Param("productNo") int productNo);
    
    // 구매 페이지 출력용
    ProductDto selectProductDetail(int productNo);
    
    // 구매 이력 가져오기 (주문 당 주문 상세, one to many)
    @Select("select o.order_no, o.order_addr, o.order_date, o.id, o.order_phone, o.total_price "
    		+ "from `order` o "
    		+ "where o.order_no = #{orderNo}")
    @Results({
    	@Result(property="orderNo", column="order_no"),
    	@Result(property="orderItems", column="order_no", 
    			many=@Many(select="com.human.rarepet.mapper.OrderMapper.selectOrderItemByOrderNo"))
    })
    OrderDto selectOrderByOrderNo(int orderNo);
    
    @Select("select * from `order` where id = #{id} order by order_date desc")
    @Results({
    	@Result(property="orderNo", column="order_no"),
    	@Result(property="orderItems", column="order_no",
    			many=@Many(select="com.human.rarepet.mapper.OrderMapper.selectOrderItemByOrderNo"))
    })
    List<OrderDto> selectAllOrderById(String id);
    
    @Select("select order_no, product_order_count, product_no, price from order_detail where order_no = #{orderNo}")
    OrderItemDto selectOrderItemByOrderNo(int orderNo); 
    
}