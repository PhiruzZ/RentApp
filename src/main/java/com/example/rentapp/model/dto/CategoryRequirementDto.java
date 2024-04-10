package com.example.rentapp.model.dto;

import com.example.rentapp.model.embedable.PropertyValue;
import com.example.rentapp.model.entity.CategoryRequirement;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.PropertyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CategoryRequirementDto {

    private Long id;
    private String propertyName;
    private PropertyType propertyType;
    private Boolean required;
    private Boolean valuesPredefined;
    private List<PropertyValue> possibleValues;

    public static List<CategoryRequirementDto> listOf(List<CategoryRequirement> categoryRequirements) {
        return categoryRequirements
                .stream()
                .filter(categoryRequirement -> categoryRequirement.getDbStatus().equals(DbStatus.ACTIVE))
                .map(categoryRequirement ->
                        CategoryRequirementDto.builder()
                                .id(categoryRequirement.getId())
                                .propertyName(categoryRequirement.getPropertyName())
                                .propertyType(categoryRequirement.getPropertyType())
                                .required(categoryRequirement.getRequired())
                                .valuesPredefined(categoryRequirement.getValuesPredefined())
                                .possibleValues(categoryRequirement.getPossibleValues())
                                .build()
                ).toList();
    }
}
