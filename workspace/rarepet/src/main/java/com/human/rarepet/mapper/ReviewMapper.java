package com.human.rarepet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.human.rarepet.dto.ReviewDto;
import java.util.List;

@Mapper
public interface ReviewMapper {
    // 리뷰 등록
    void insertReview(ReviewDto review);
    
    // 리뷰 목록 조회
    List<ReviewDto> selectAllReviews();

    // 특정 리뷰 상세 조회 (수정 페이지에서 사용)
    ReviewDto selectReviewByNo(Long reviewNo);

    // 리뷰 수정 실행 (업데이트)
    void updateReview(ReviewDto review);

    // 리뷰 삭제 실행
    void deleteReview(Long reviewNo);
    
    @Select("select * from review where product_no = #{productNo}")
    List<ReviewDto> selectReviewsByNo(int productNo);
}