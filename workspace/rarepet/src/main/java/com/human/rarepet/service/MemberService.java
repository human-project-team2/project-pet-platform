package com.human.rarepet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ProductImageDto;
import com.human.rarepet.dto.RoleDto;
import com.human.rarepet.dto.FavoriteListDto;
import com.human.rarepet.mapper.MemberMapper;
import com.human.rarepet.mapper.ProductMapper;
import com.human.rarepet.security.RarepetPasswordEncoder;

@Service("memberService")
public class MemberService {
	
	
	private MemberMapper memberMapper;
	
	private ProductMapper productMapper;
	
	private PasswordEncoder passwordEncoder;
	
	public MemberService(MemberMapper memberMapper, 
			ProductMapper productMapper, 
			PasswordEncoder passwordEncoder) {
		this.memberMapper = memberMapper;
		this.productMapper = productMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	
	public void insertMember(MemberDto member) {
		
		String encoded = passwordEncoder.encode(member.getPasswd());
		member.setPasswd(encoded);
		
		// 3. mybatis 사용
		memberMapper.insertMember(member);
		for (RoleDto role : member.getRoles()) {
			memberMapper.insertMemberRole(member.getId(), role.getRoleNo());
		}
		
	}
	
	public MemberDto checkMember(String memberId, String passwd) {
		
		return memberMapper.checkMember(memberId, passwd);
		
	}
	
	public MemberDto selectMember(String id) {
		return memberMapper.selectMemberById(id);
	}
	// 아이디 중복 확인 로직
    public boolean existsById(String id) {
        // DB에서 해당 ID로 조회했을 때 결과가 null이 아니면 이미 존재하는 것(중복)
        MemberDto member = memberMapper.selectMemberById(id);
        return member != null; 
    }
	

	
	
	// [추가] 마이페이지용 회원 정보 가져오기
    public MemberDto getUserById(String id) {
        return memberMapper.getUserById(id);
    }
    
 // [추가] 회원 정보 업데이트 로직
    public void updateMember(MemberDto member) {
        // 매퍼를 호출해서 DB 값을 바꿉니다.
        memberMapper.updateMember(member);
    }

	public List<RoleDto> selectMemberRolesById(String id) {
		return memberMapper.selectMemberRolesById(id);
	}
	
	public boolean checkName(String memberName) {
		MemberDto member = memberMapper.checkName(memberName);
		return member != null;
	}
	
	// 관심 등록된 상품 관련
	public void addFavoriteList(String id, int productId) {
		memberMapper.addFavoriteList(id, productId);
	}
	
	public boolean checkFavoriteList(String id, int productId) {
		return memberMapper.checkFavoriteList(id, productId) != null;
	}
	
	public List<FavoriteListDto> selectAllFavorites(String id){
		
		List<FavoriteListDto> myFavorites = memberMapper.selectAllFavorites(id);
		List<FavoriteListDto> myFavorites2 = new ArrayList<>();
		
		for(FavoriteListDto favorite : myFavorites ) {
			int productNo = favorite.getProductId();
			ProductDto product = productMapper.selectProductByNo(productNo);
			
			if(product != null) {
			List<ProductImageDto> images = product.getProductImages();
			if(images != null && !images.isEmpty()) {
				favorite.setImgUrl(images.get(0).getImgUrl());
			}
			favorite.setProductName(product.getProductName());
			favorite.setPrice(product.getPrice());
			myFavorites2.add(favorite);
			}
		}
		
		return myFavorites2;
	}
	
}
