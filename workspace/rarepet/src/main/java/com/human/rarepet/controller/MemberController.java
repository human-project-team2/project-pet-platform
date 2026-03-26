package com.human.rarepet.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; // 추가됨

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.RoleDto;
import com.human.rarepet.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@GetMapping(path = { "/login" })
	public String login() {
		return "/auth/login";
	}

// spring security 이용하여 대체됨.
//	@PostMapping(path = {"/login"})
//	public String loginProcess(@RequestParam("memberId") String memberId,
//							   @RequestParam("passwd") String passwd,
//							   HttpSession session){
//		
//		MemberDto member = memberService.checkMember(memberId, passwd); 
//		if (member != null) {
//			session.setAttribute("loginuser", member);
//			
//			String redirectUrl = (String)session.getAttribute("redirectUrl");
//			if (redirectUrl != null) {
//				session.removeAttribute("redirectUrl");
//				return "redirect:" + redirectUrl;
//			}
//			return "redirect:/";
//		} else {
//			return "auth/login";
//		}
//	}

	// spring security 이용하여 대체됨.
//	@GetMapping(path = {"/logout"})
//	public String logout(HttpSession session) {
//		session.removeAttribute("loginuser");
//		session.removeAttribute("redirectUrl");
//		return "redirect:login";
//	}

	@GetMapping(path = { "/register" })
	public String register() {
		return "/auth/register";
	}

	
	@PostMapping("/register")
	public String registerProcess(@ModelAttribute MemberDto member) {
		
		List<RoleDto> roles = new ArrayList<RoleDto>();
		RoleDto role = new RoleDto();
		role.setRoleNo(2); // 일반 사용자 역할 번호
		role.setRoleName("member");
		roles.add(role);
		member.setRoles(roles);
		
		memberService.insertMember(member);
		return "redirect:/auth/login";
	}

	/**
	 * 아이디 중복 검사 엔드포인트
	 * @ResponseBody를 붙여서 HTML 페이지 이동이 아닌 '데이터'만 응답함
	 */
	@GetMapping("/dup-check")
	@ResponseBody
	public String duplicateCheck(@RequestParam("id") String id) {
		// memberService에 해당 ID로 사용자를 조회하는 메서드가 필요합니다.
		// 존재하면 true(중복), 없으면 false를 반환한다고 가정합니다.
		boolean isDuplicate = memberService.existsById(id); 
		
		if (isDuplicate) {
			return "used";   // 이미 존재하는 아이디
		} else {
			return "valid";  // 사용 가능한 아이디
		}
	}
	
	@GetMapping(path = {"/name-check"})
	@ResponseBody
	public String nameCheck(@RequestParam("memberName") String memberName) {
		
		boolean alreadyInUse = memberService.checkName(memberName);
		if(alreadyInUse) {
			return "used";
		} else {
			return "valid";
		}
	}
	


}