package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.UserReview;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class UserReviewDto {

    private Long reviewerUserId;
    private Long reviewedUserId;
    private Integer numberOfStars;
    private String comment;

    public static List<UserReviewDto> listOf(List<UserReview> userReviews) {
        return userReviews.stream()
                .map(userReview -> UserReviewDto.builder()
                        .reviewedUserId(userReview.getReviewedUser().getId())
                        .reviewerUserId(userReview.getReviewerUser().getId())
                        .comment(userReview.getComment())
                        .numberOfStars(userReview.getNumberOfStars())
                        .build()
                ).toList();
    }
}
