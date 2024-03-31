package com.example.rentapp.model.request;

import com.example.rentapp.model.enums.OTPPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendOTPRequest {

    @NotBlank(message = "email is mandatory!")
    private String email;

    @NotNull(message = "purpose is mandatory!")
    private OTPPurpose purpose;

}
