package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.ProductProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductPropertiesDto {

    private String propertyName;
    private String propertyValue;

    public static List<ProductPropertiesDto> listOf(List<ProductProperties> properties) {
        return properties.stream()
                .map(productProperties -> ProductPropertiesDto.builder()
                        .propertyName(productProperties.getPropertyName())
                        .propertyValue(productProperties.getPropertyValue())
                        .build())
                .toList();
    }
}
