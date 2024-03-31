package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateDocumentTypeRequest {

    @NotBlank
    private String name;

    @NotNull
    private Boolean allowedForUserVerification;

}
