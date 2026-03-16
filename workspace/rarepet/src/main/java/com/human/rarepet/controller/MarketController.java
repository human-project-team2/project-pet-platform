package com.human.rarepet.controller;



import java.io.File;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ProductImageDto;
import com.human.rarepet.dto.ShopDto;
import com.human.rarepet.dto.ShoppingCartDto;
import com.human.rarepet.dto.UpdateCartQuantityDto;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.MarketService;
import com.human.rarepet.service.MemberService;
import com.human.rarepet.service.ShopService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class MarketController {
	
	private final MemberService memberService;
	private final MarketService marketService;
	private final ShopService shopService;
	
	public MarketController(MarketService marketService, MemberService memberService,
			                ShopService shopService) {
		this.memberService = memberService;
		this.marketService = marketService;
		this.shopService = shopService;
	}
	
	@GetMapping(path={"/market"})
	public String market(Model model,
			             @RequestParam(value = "search", required = false) String search,
			             @RequestParam(value = "category", required = false) String category) {
	
		//List<ProductDto> products = marketService.showAllProduct();
		List<ProductDto> products = marketService.searchProducts(search, category);
		List<ShopDto> shops = shopService.selectAllShops();
		
		model.addAttribute("category", category);
		model.addAttribute("search", search);
		model.addAttribute("products", products);
		model.addAttribute("shops", shops);
		System.out.println(shops);
		return "market/list";
	}
	
	// 제품 상세
	@GetMapping(path="/market/detail/{productNo}")
	public String marketDetail(@PathVariable("productNo") int productNo,
								Model model) {
		
		ProductDto product = marketService.selectProductByNo(productNo);
		
		String id = product.getSellerId();
		MemberDto member = memberService.selectMember(id);
		
		model.addAttribute("phone", member.getPhone());
		model.addAttribute("email", member.getEmail());
		model.addAttribute("product", product);
		
		return "market/detail";
	}
	
	@GetMapping(path= {"/market/register"})
	public String register(@AuthenticationPrincipal RarepetUserDetails userDetails, RedirectAttributes redirectMessage) {
		
		MemberDto member = userDetails.getMember();
		
		if(!member.isShop()) {
			redirectMessage.addFlashAttribute("shopRegisterMessage", "판매자 등록된 회원만 등록이 가능합니다. -> 마이페이지");
			return "redirect:/mypage";
		} else {
			return "market/register";
		}
	}
	
	final String UPLOAD_PATH = "d:\\uploaded_images";
	
	@PostMapping(path = {"/market/register"})
	public String registerProduct(ProductDto product,
			@RequestParam("picture") List<MultipartFile> pictures,
			@AuthenticationPrincipal RarepetUserDetails userDetails,
			RedirectAttributes redirectAttributes ) {
				
		MemberDto member = userDetails.getMember();
		String id = member.getId();
		product.setSellerId(id);
		boolean success = marketService.registerProduct(product, pictures);
		if(success) {
			redirectAttributes.addFlashAttribute("registered", "상품이 등록되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("registered", "상품이 등록 중 오류가 발생했습니다.");
		}
		
		return "redirect:/market/detail/" + product.getProductNo();
	}
	
	@GetMapping(path = {"/market/shoppingCart"})
	public String shoppingCart(Model model,
			@AuthenticationPrincipal RarepetUserDetails userDetails) {
		
		MemberDto member = userDetails.getMember();
		
		if(member == null) {
			return "redirect:/auth/login";
		} 
		String id = member.getId();
		
		List<ShoppingCartDto> cartList = marketService.selectShoppingCart(id);
		
		model.addAttribute("cartList", cartList);
		return "market/shoppingCart";
	}
	
	@PostMapping(path = {"/market/shoppingCart"})
	public String addShoppingCart(@RequestParam("productNo") int productNo, 
			@AuthenticationPrincipal RarepetUserDetails userDetails,
									RedirectAttributes redirectAttributes) {
		
		MemberDto member = userDetails.getMember();
		if(member == null) {
			return "redirect:/auth/login";
		} 
		String id = member.getId();

		try {
			marketService.addShoppingCart(productNo, id);
			redirectAttributes.addFlashAttribute("message", "장바구니에 담겼습니다.");
			
		} catch (DuplicateKeyException exception) {
			redirectAttributes.addFlashAttribute("message", "이미 장바구니에 담긴 상품입니다");
			
		}
		
		return "redirect:/market/shoppingCart";
	}
	
	@GetMapping(path = {"/market/deleteShoppingCart"})
	public String deleteShoppingCart(@RequestParam("productNo") int productNo,
			@AuthenticationPrincipal RarepetUserDetails userDetails) {
		
		MemberDto member = userDetails.getMember();
		String id = member.getId();
		marketService.deleteShoppingCart(productNo, id);
		return "redirect:/market/shoppingCart";
	}
	
	@PostMapping(path = {"/market/updateShoppingCart"})
	@ResponseBody
	public String updateShoppingCart(@RequestBody List<UpdateCartQuantityDto> updatedList,
			@AuthenticationPrincipal RarepetUserDetails userDetails,
									 RedirectAttributes redirectAttributes) {
		
		MemberDto member = userDetails.getMember();

		if(member != null) {
			String id = member.getId();
			marketService.updateShoppingCartList(id, updatedList);
			return "success";
		} else {
			return "failure";
		}
	}
}
