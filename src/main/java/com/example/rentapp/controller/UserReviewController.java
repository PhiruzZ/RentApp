package com.example.rentapp.controller;

import com.example.rentapp.model.dto.UserReviewDto;
import com.example.rentapp.model.request.CreateUserReviewRequest;
import com.example.rentapp.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class UserReviewController {

    private final UserReviewService userReviewService;

    @PostMapping
    public ResponseEntity<Void> addReview(@RequestBody CreateUserReviewRequest request){
        userReviewService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserReviewDto>> getByUserId(@RequestParam Long userId){
        return ResponseEntity.ok(userReviewService.getByUserId(userId));
    }

}
