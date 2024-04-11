package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyOTPRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String otp;

}
