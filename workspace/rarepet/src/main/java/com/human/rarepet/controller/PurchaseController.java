package com.human.rarepet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.rarepet.dto.ShoppingCartDto; // 꽃게님 프로젝트의 DTO명으로 수정
import com.human.rarepet.dto.UpdateCartQuantityDto;
import com.human.rarepet.exception.ZeroProductException;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.OrderDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.MarketService; // 주입 대상 수정
import com.human.rarepet.service.OrderService;

@Controller
public class PurchaseController {

    
    private final OrderService orderService;
    
    private final MarketService marketService;
    
    public PurchaseController(OrderService orderService, MarketService marketService) {
    	this.orderService = orderService;
    	this.marketService = marketService;
    }

    // 1. 단일 상품 구매 (상세페이지 -> 결제)
    @GetMapping("/purchase/{productNo}")
    public String purchasePage(@PathVariable("productNo") int productNo, 
                               Authentication authentication, Model model) {
        
        RarepetUserDetails userDetails = (RarepetUserDetails) authentication.getPrincipal();
        MemberDto member = userDetails.getMember();

        // MarketService의 메서드 사용
        ProductDto product = marketService.selectProductByNo(productNo);
        
        model.addAttribute("member", member);
        model.addAttribute("product", product);
        model.addAttribute("isCartOrder", false); 
        
        return "order/purchase";
    }

    
    @GetMapping("/purchase/cart")
    public String purchaseCartPage(Authentication authentication, Model model) {
        
        RarepetUserDetails userDetails = (RarepetUserDetails) authentication.getPrincipal();
        MemberDto member = userDetails.getMember();
        
        List<ShoppingCartDto> cartList = marketService.selectShoppingCart(member.getId()); 

        if (cartList == null || cartList.isEmpty()) {
            return "redirect:/market/shoppingCart"; 
        }

        // 총 결제 금액 계산
        int totalPrice = cartList.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getCartQuantity())
                .sum();

        model.addAttribute("member", member);
        model.addAttribute("cartList", cartList); // ShoppingCartDto 리스트 전달
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("isCartOrder", true);

        return "order/purchase";
    }

    // 3. 주문 처리 (POST)
    @PostMapping("/order/process")
    public String processOrder(OrderDto order, Authentication authentication, RedirectAttributes rttr) {
        
        RarepetUserDetails userDetails = (RarepetUserDetails) authentication.getPrincipal();
        MemberDto member = userDetails.getMember();
        
        order.setId(member.getId());

        try {
            // orderService에서 주문 저장 + 장바구니 비우기 로직이 실행되어야 함
            int generatedOrderNo = orderService.executeOrder(order);
            System.out.println(order);
            rttr.addFlashAttribute("orderNo", generatedOrderNo);
            rttr.addFlashAttribute("address", order.getOrderAddr());
            
            return "redirect:/order/success/" + order.getOrderNo();
            
        } catch (ZeroProductException e) {
            e.printStackTrace();
            rttr.addFlashAttribute("msg", e.getMessage());
            return "redirect:/market/shoppingCart";
        }
    }

    @GetMapping("/order/success/{orderNo}")
    public String orderSuccess(@PathVariable("orderNo") int orderNo, Model model) {
    	
    	OrderDto ordered = orderService.selectOrderByOrderNo(orderNo);
    	model.addAttribute("ordered", ordered);
    	
        return "/order/order-success";
    }
}