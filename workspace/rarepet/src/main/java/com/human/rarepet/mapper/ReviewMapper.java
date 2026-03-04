package com.human.rarepet.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.human.rarepet.dto.ReviewDto;
import java.util.List;

@Mapper
public interface ReviewMapper {
    // 리뷰 등록
    void insertReview(ReviewDto review);
    
    // 리뷰 목록 조회 (추가)
    List<ReviewDto> selectAllReviews();
}