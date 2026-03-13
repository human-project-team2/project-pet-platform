package com.human.rarepet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
	
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						auth -> auth
									.requestMatchers("/chatRoom").authenticated()
									.requestMatchers("/market/register","/market/shoppingCart",
											"/market/deleteShoppingCart","/market/updateShoppingCart","/mypage").hasRole("member")
									.requestMatchers("/messages/**").hasRole("member")
									.requestMatchers("/review/write").hasRole("member")
									.requestMatchers("/shop-register").hasRole("member")
									.requestMatchers("/shop/**").authenticated()
									.requestMatchers("/admin").hasRole("admin")
									.anyRequest().permitAll())
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(form -> form.loginPage("/auth/login")
										.loginProcessingUrl("/auth/login")
										.usernameParameter("memberId")
										.passwordParameter("passwd")
                                        //.defaultSuccessUrl("/", false) // 로그인 성공 후 리다이렉트할 URL, false는 항상 이 URL로 리다이렉트
                                        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler()) // 로그인 성공 후 이전 요청으로 리다이렉트
										.failureUrl("/auth/login?error=true"))
                           
				.logout(logout -> logout.logoutUrl("/auth/logout")
						                .invalidateHttpSession(true)
						                .deleteCookies("JSESSIONID")
										.logoutSuccessUrl("/"));
				
		return http.build();
						
	}
	
	@Bean 
	PasswordEncoder passwordEncoder() {
		return new RarepetPasswordEncoder();
	}
	
	@Bean 
	UserDetailsService userDetailsService(MemberService memberService) {
		return new RarepetUserDetailsService(memberService);
	}
	
}
