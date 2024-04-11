package com.example.rentapp.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserReviewRequest {

    @NotNull
    private Long agreementRequestId;
    private String comment;

    @Max(5)
    @Min(0)
    @NotNull
    private Integer numberOfStars;

}
