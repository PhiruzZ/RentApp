package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateAgreementRequestRequest {

    @NotNull
    private Long productId;
    @NotNull
    private LocalDate fromDate;
    @NotNull
    private LocalDate toDate;

}
