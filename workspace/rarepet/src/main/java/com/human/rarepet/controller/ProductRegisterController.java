package com.human.rarepet.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ProductRegisterController {

    @GetMapping(path = {"/productRegister"})
    public String product() {
        return "productRegister";
    }
    
  
}
