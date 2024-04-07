package com.example.rentapp.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateAgreementRequestRequest {

    private Long productId;
    private LocalDate fromDate;
    private LocalDate toDate;

}
