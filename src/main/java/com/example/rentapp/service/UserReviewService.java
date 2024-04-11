package com.example.rentapp.service;

import com.example.rentapp.model.dto.UserReviewDto;
import com.example.rentapp.model.entity.AgreementRequest;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.entity.UserReview;
import com.example.rentapp.model.enums.AgreementRequestStatus;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.CreateUserReviewRequest;
import com.example.rentapp.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;
    private final AgreementRequestService agreementRequestService;
    private final AuthService authService;

    @Transactional
    public void create(CreateUserReviewRequest request) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest agreementRequest = agreementRequestService.findByIdAndUserId(request.getAgreementRequestId(), user.getId());
        if(!agreementRequest.getStatus().equals(AgreementRequestStatus.FINALISED)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't review user with request status: " + agreementRequest.getStatus());
        }
        createUserReview(request, user, agreementRequest);
    }

    private void createUserReview(CreateUserReviewRequest request, UserEntity user, AgreementRequest agreementRequest) {
        UserReview userReview = new UserReview();
        userReview.setReviewerUser(user);
        userReview.setReviewedUser(user.getId().equals(agreementRequest.getFromUser().getId()) ? agreementRequest.getProduct().getOwner() : agreementRequest.getFromUser());
        userReview.setComment(request.getComment());
        userReview.setAgreementRequest(agreementRequest);
        userReview.setNumberOfStars(request.getNumberOfStars());
        userReviewRepository.save(userReview);
    }

    public List<UserReviewDto> getByUserId(Long userId) {
        List<UserReview> userReviews = userReviewRepository.findByReviewedUserIdAndDbStatus(userId, DbStatus.ACTIVE);
        return UserReviewDto.listOf(userReviews);
    }
}
