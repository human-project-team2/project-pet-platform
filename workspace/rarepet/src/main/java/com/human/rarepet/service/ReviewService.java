package com.human.rarepet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.human.rarepet.dto.ReviewDto;
import com.human.rarepet.mapper.ReviewMapper;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    // 리뷰 등록
    public void registerReview(ReviewDto review) {
        reviewMapper.insertReview(review);
    }

    // 리뷰 전체 목록 조회
    public List<ReviewDto> getReviewList() {
        return reviewMapper.selectAllReviews();
    }
    
    // --- 아래 메서드들을 추가합니다 ---

    // 1. 특정 리뷰 상세 조회 (수정 화면에 기존 내용을 보여주기 위해 필요)
    public ReviewDto getReviewByNo(Long reviewNo) {
        return reviewMapper.selectReviewByNo(reviewNo);
    }

    // 2. 리뷰 수정 실행
    public void modifyReview(ReviewDto review) {
        reviewMapper.updateReview(review);
    }

    // 3. 리뷰 삭제 실행
    public void removeReview(Long reviewNo) {
        reviewMapper.deleteReview(reviewNo);
    }
    
    public List<ReviewDto> selectReviewsByNo(int productNo){
    	
    	return reviewMapper.selectReviewsByNo(productNo);
    }
}