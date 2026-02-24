package com.human.rarepet.controller;

import com.human.rarepet.dto.ProductRegisterDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ProductRegisterController {

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/productRegister")
    public String product() {
        return "productRegister";
    }

    @PostMapping("/productRegister")
    public String registerProcess(@ModelAttribute ProductRegisterDto dto) {
        
        // 1. 파일 업로드 처리 (첫 번째 이미지를 대표 이미지로 저장하는 예시)
        String savedFileName = "";
        String originalFileName = "";
        String imgUrl = "";

        if (dto.getPetImages() != null && !dto.getPetImages().isEmpty()) {
            MultipartFile firstFile = dto.getPetImages().get(0);
            if (!firstFile.isEmpty()) {
                originalFileName = firstFile.getOriginalFilename();
                savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
                
                // 실제 저장 경로 (프로젝트 외부에 저장하는 것을 권장하나 임시로 프로젝트 내 static 폴더 설정)
                String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                File saveFile = new File(uploadPath, savedFileName);
                
                try {
                    if (!saveFile.exists()) saveFile.mkdirs();
                    firstFile.transferTo(saveFile);
                    imgUrl = "/uploads/" + savedFileName;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 2. DB 저장을 위한 ProductDto 생성 및 매핑
        ProductDto product = new ProductDto();
        product.setProductName(dto.getTitle());
        product.setProductCategory(dto.getCategory());
        product.setProductCount(dto.getProductCount());
        product.setPrice(dto.getPrice()); // 숫자로 변환 필요시 처리
        product.setProductContent(dto.getContent());
        product.setOriginalFileName(originalFileName);
        product.setSavedFileName(savedFileName);
        product.setImgUrl(imgUrl);
        // 필요시 shortDescription 등 추가 세팅
        
        // 3. DB Insert
        productMapper.insertProduct(product);

        System.out.println("등록 완료: " + dto.getTitle());

        return "redirect:/home"; // 홈으로 이동
    }
}