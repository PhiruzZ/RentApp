package com.example.rentapp.model.request;

import com.example.rentapp.model.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CreateCategoryRequirementRequest {

    @NotBlank
    private String propertyName;
    @NotNull
    private PropertyType propertyType;
    private Boolean required = false;
    @NotNull
    private Boolean valuesPredefined;
    private List<CreatePropertyValueRequest> possibleValues;

}
