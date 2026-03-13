package com.human.rarepet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.human.rarepet.security.RarepetPasswordEncoder;
import com.human.rarepet.security.RarepetUserDetailsService;
import com.human.rarepet.service.MemberService;

@Configuration
@EnableWebSecurity 
public class RarepetSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (테스트 및 개발 편의성)
				.authorizeHttpRequests(
						auth -> auth

									.requestMatchers("/chatRoom").authenticated()
									

									// 1. 회원(member) 권한이 필요한 경로들 (구매/주문 경로 추가됨)
									.requestMatchers(
											"/purchase/**", 
											"/order/**",
											"/market/register",
											"/market/shoppingCart",
											"/market/deleteShoppingCart",
											"/market/updateShoppingCart",
											"/mypage"
									).hasRole("member")
									
									// 2. 기타 회원 전용 기능

									.requestMatchers("/messages/**").hasRole("member")
									.requestMatchers("/review/write").hasRole("member")
									.requestMatchers("/shop-register").hasRole("member")
									
									// 3. 인증만 되면 접근 가능한 경로
									.requestMatchers("/shop/**").authenticated()
									
									// 4. 관리자 전용 경로
									.requestMatchers("/admin").hasRole("admin")
									
									// 5. 그 외 나머지 모든 요청(메인, 로그인 등)은 누구나 허용
									.anyRequest().permitAll())
				
				.httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP 인증 비활성화
				
				.formLogin(form -> form.loginPage("/auth/login") // 커스텀 로그인 페이지 설정
										.loginProcessingUrl("/auth/login") // 로그인 처리 URL
										.usernameParameter("memberId") // 아이디 파라미터명
										.passwordParameter("passwd") // 비밀번호 파라미터명
                                        // 로그인 성공 시 이전 요청 페이지로 자동 리다이렉트 (예: 구매페이지 시도 -> 로그인 -> 다시 구매페이지)
                                        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler()) 
										.failureUrl("/auth/login?error=true")) // 로그인 실패 시 이동할 URL
                           
				.logout(logout -> logout.logoutUrl("/auth/logout") // 로그아웃 처리 URL
						                .invalidateHttpSession(true) // 세션 무효화
						                .deleteCookies("JSESSIONID") // 쿠키 삭제
										.logoutSuccessUrl("/")); // 로그아웃 성공 시 메인으로
				
		return http.build();
						
	}
	
	@Bean 
	PasswordEncoder passwordEncoder() {
		// 사용자 정의 패스워드 인코더 (RarepetPasswordEncoder) 사용
		return new RarepetPasswordEncoder();
	}
	
	@Bean 
	UserDetailsService userDetailsService(MemberService memberService) {
		// 사용자 정의 유저 디테일 서비스 사용
		return new RarepetUserDetailsService(memberService);
	}
	
}