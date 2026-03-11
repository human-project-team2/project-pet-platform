package com.human.rarepet.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.human.rarepet.dto.ReviewDto;
import java.util.List;

@Mapper
public interface ReviewMapper {
    // 리뷰 등록
    void insertReview(ReviewDto review);
    
    // 리뷰 목록 조회
    List<ReviewDto> selectAllReviews();

    // --- 아래 메서드들을 추가합니다 ---

    // 특정 리뷰 상세 조회 (수정 폼 데이터 바인딩용)
    ReviewDto selectReviewByNo(Long reviewNo);

    // 리뷰 수정 실행 (업데이트)
    void updateReview(ReviewDto review);

    // 리뷰 삭제 실행
    void deleteReview(Long reviewNo);
}