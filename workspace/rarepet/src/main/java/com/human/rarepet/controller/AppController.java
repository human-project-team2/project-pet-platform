package com.human.rarepet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.service.MemberService;
import com.human.rarepet.service.MarketService; // MarketService 임포트 추가

@RestController
@CrossOrigin(origins = "*") 
@RequestMapping("/api")
public class AppController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MarketService marketService; // 💡 MarketService 주입 추가

    /**
     * Flutter 앱에서 특정 유저 정보를 요청할 때 사용
     */
    @GetMapping("/user/{id}")
    public MemberDto getUserData(@PathVariable("id") String id) {
        return memberService.getUserById(id);
    }
    
    /**
     * [추가] 모든 상품 리스트를 Flutter로 던져줍니다.
     * Flutter 주소: http://10.0.2.2:8080/api/products/all
     */
    @GetMapping("/products/all")
    public List<ProductDto> getAllProducts() {
        // DB의 product_all 테이블에 있는 모든 데이터를 가져옵니다.
        return marketService.showAllProduct();
    }

    /**
     * [추가] 특정 유저가 등록한 상품만 가져올 때 사용 (마이페이지용)
     */
    @GetMapping("/products/user/{id}")
    public List<ProductDto> getProductsByUser(@PathVariable("id") String id) {
        return marketService.findProductsByUserId(id);
    }
    
    /**
     * 연결 확인용
     */
    @GetMapping("/hello")
    public String hello() {
        return "AnyZoo 서버가 정상적으로 응답하고 있습니다! 🐾";
    }
}