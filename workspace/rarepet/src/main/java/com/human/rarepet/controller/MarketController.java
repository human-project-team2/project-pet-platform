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
import com.human.rarepet.dto.ReviewDto;
import com.human.rarepet.dto.ShopDto;
import com.human.rarepet.dto.ShoppingCartDto;
import com.human.rarepet.dto.UpdateCartQuantityDto;
import com.human.rarepet.exception.AlreadyInCartException;
import com.human.rarepet.exception.ZeroProductException;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.MarketService;
import com.human.rarepet.service.MemberService;
import com.human.rarepet.service.ReviewService;
import com.human.rarepet.service.ShopService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class MarketController {
	
	private final MemberService memberService;
	private final MarketService marketService;
	private final ShopService shopService;
	private final ReviewService reviewService;
	public MarketController(MarketService marketService, MemberService memberService,
			                ShopService shopService,
			                ReviewService reviewService) {
		this.memberService = memberService;
		this.marketService = marketService;
		this.shopService = shopService;
		this.reviewService = reviewService;
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
		
		return "market/list";
	}
	
	// 제품 상세
	@GetMapping(path="/market/detail/{productNo}")
	public String marketDetail(@PathVariable("productNo") int productNo,
								Model model,
								@AuthenticationPrincipal RarepetUserDetails userDetails) {
		
		
		ProductDto product = marketService.selectProductByNo(productNo);
		String id = product.getSellerId();
		MemberDto member = memberService.selectMember(id);
		boolean favorited = false;
		if(userDetails != null) {
			String idLogged = userDetails.getMember().getId();
			favorited = memberService.checkFavoriteList(idLogged, productNo);
		}
		
		List<ReviewDto> reviews = reviewService.selectReviewsByNo(productNo);
		
		model.addAttribute("reviews", reviews);
		model.addAttribute("isFavorite", favorited);
		model.addAttribute("phone", member.getPhone());
		model.addAttribute("email", member.getEmail());
		model.addAttribute("product", product);
		model.addAttribute("productId", product.getProductNo());
		
		return "market/detail";
	}
	
	@GetMapping(path= {"/market/register"})
	public String register(@AuthenticationPrincipal RarepetUserDetails userDetails, RedirectAttributes redirectMessage) {
		
		MemberDto member = userDetails.getMember();
		ShopDto shop = null;
		
		
		
		if(shopService.showShopInfo(member.getId()) == null) {
			redirectMessage.addFlashAttribute("shopRegisterMessage", "판매자 등록된 회원만 등록이 가능합니다. -> 마이페이지");
			return "redirect:/mypage";
			
		} else if(shopService.showShopInfo(member.getId()) != null) { 
			shop = shopService.showShopInfo(member.getId());
			if(!shop.isVerified()) {
				redirectMessage.addFlashAttribute("shopRegisterMessage", "판매자 승인 대기중 입니다.");
				return "redirect:/mypage";
			}	
			return "market/register";
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
	
	@PostMapping(path = {"/market/favorite"})
	
	public String addFavorite(@RequestParam("productId") int productId, Model model,
							  @AuthenticationPrincipal RarepetUserDetails userDetails) {

		if(userDetails != null) {
			String id = userDetails.getMember().getId();
			
			memberService.addFavoriteList(id, productId);
			model.addAttribute("isFavorite", true);
	        
		} else {
			model.addAttribute("productId", productId); // Pass ID back so the hidden input still works
			model.addAttribute("errorMessage", "로그인 한 사용자만 가능합니다.");
			model.addAttribute("isFavorite", false);
		}
		
		// 형식 : "template_name :: fragment_name"
		return "market/detail :: favorite-btn-area";
		
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
		} catch (AlreadyInCartException exception) {
			redirectAttributes.addFlashAttribute("message", exception.getMessage());
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
		String id = member.getId();
		marketService.updateShoppingCartList(id, updatedList);
		
		return "success"; 
	}
	
	@GetMapping(path = {"/market/shop/{id}"})
	public String getShopPage(@PathVariable("id") String id, Model model) {
		
		List<ProductDto> products = marketService.findProductsByUserId(id);
		ShopDto shop = shopService.showShopInfo(id);
		model.addAttribute("shop", shop);
		model.addAttribute("shopProducts", products);
		return "market/shop";
	}
}
