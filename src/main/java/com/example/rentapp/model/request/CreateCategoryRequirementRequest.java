package com.example.rentapp.model.request;

import com.example.rentapp.model.enums.PropertyType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CreateCategoryRequirementRequest {

    private String propertyName;
    private PropertyType propertyType;
    private Boolean required = false;
    private Boolean valuesPredefined;
    private List<CreatePropertyValueRequest> possibleValues;

}
