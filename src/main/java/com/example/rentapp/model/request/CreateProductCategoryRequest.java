package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CreateProductCategoryRequest {

    @NotBlank(message = "name is mandatory!")
    private String name;

    private List<Long> documentTypeIds;

}
