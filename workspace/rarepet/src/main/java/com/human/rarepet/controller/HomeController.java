package com.human.rarepet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.service.BoardService;
import com.human.rarepet.service.MarketService;

@Controller
public class HomeController {
    
    @Autowired
    private MarketService marketService;

    @Autowired
    private BoardService boardService;
    
    @GetMapping(path = {"/", "/home"})
    public String home(
            @RequestParam(value = "page", defaultValue = "1") int page, // 페이지 번호 추가
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            Model model) {
        
        // 1. 최신 상품 6개 가져오기 (MarketService 인스턴스 사용)
        List<ProductDto> latestProducts = marketService.getLatestProducts(page);
        
        // 2. 모델에 데이터 담기
        model.addAttribute("latestProducts", latestProducts);
        model.addAttribute("currentPage", page);
        
        // 3. 인기글 데이터 가져오기 (BoardService 인스턴스 사용)
        List<BoardDto> dailyTrending = boardService.getTrendingBoards(1);
        model.addAttribute("dailyTrending", dailyTrending);
        
        return "home";
    }
    
    @GetMapping("/api/trending")
    @ResponseBody 
    public List<BoardDto> getTrendingApi(@RequestParam(value = "days", defaultValue = "1") int days) {
        return boardService.getTrendingBoards(days);
    }
}