package com.example.rentapp.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyOTPRequest {

    private String email;
    private String otp;

}
