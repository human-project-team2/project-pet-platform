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

    public void registerReview(ReviewDto review) {
        reviewMapper.insertReview(review);
    }

    public List<ReviewDto> getReviewList() {
        return reviewMapper.selectAllReviews();
    }
}