package com.example.rentapp.repository;

import com.example.rentapp.model.entity.UserReview;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByReviewedUserIdAndDbStatus(Long userId, DbStatus dbStatus);
}
