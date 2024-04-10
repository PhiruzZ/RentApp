package com.example.rentapp.model.request;

import com.example.rentapp.model.enums.OTPPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendOTPRequest {

    @NotBlank(message = "email is mandatory!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotNull(message = "purpose is mandatory!")
    private OTPPurpose purpose;

}
