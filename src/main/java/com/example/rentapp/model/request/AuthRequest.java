package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {

    @NotBlank(message = "password is mandatory!")
    private String password;

    @NotBlank(message = "verification id is mandatory!")
    private String verificationId;

}
