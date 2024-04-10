package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationRequest {

    @NotBlank(message = "verification id is mandatory!")
    private String verificationId;

    @NotBlank(message = "first name is mandatory!")
    private String firstName;

    @NotBlank(message = "last name is mandatory!")
    private String lastName;

    @NotBlank(message = "password is mandatory!")
    private String password;

    @NotBlank(message = "phone number is mandatory!")
    @Pattern(regexp = "^5\\d{8}$")
    private String phoneNumber;

}
