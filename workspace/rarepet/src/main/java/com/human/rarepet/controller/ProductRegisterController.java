package com.human.rarepet.controller;

import com.human.rarepet.dto.ProductRegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductRegisterController {

    @GetMapping("/productRegister")
    public String product() {
        return "productRegister";
    }

    @PostMapping("/productRegister")
    public String registerProcess(@ModelAttribute ProductRegisterDto dto) {
        
        // 데이터 수신 확인을 위한 콘솔 출력
        System.out.println("===== 상품 등록 데이터 수신 확인 =====");
        System.out.println("상품명: " + dto.getTitle());
        System.out.println("카테고리: " + dto.getCategory());
        System.out.println("가격: " + dto.getPrice());
        System.out.println("설명: " + dto.getContent());
        
        if (dto.getPetImages() != null) {
            System.out.println("업로드된 이미지 개수: " + dto.getPetImages().size());
            dto.getPetImages().forEach(file -> {
                if (!file.isEmpty()) {
                    System.out.println("파일명: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");
                }
            });
        }
        System.out.println("==================================");

        // 성공 시 리다이렉트 (임시로 홈으로 이동)
        return "redirect:home";
    }
}