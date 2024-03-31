package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.CategoryRequirement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CategoryRequirementDto {

    private Long id;
    private DocumentTypeDto documentType;

    public static List<CategoryRequirementDto> listOf(List<CategoryRequirement> categoryRequirements) {
        return categoryRequirements
                .stream()
                .map(categoryRequirement -> CategoryRequirementDto.builder()
                                .id(categoryRequirement.getId())
                                .documentType(DocumentTypeDto.of(categoryRequirement.getDocumentType()))
                                .build()
                ).toList();
    }
}
