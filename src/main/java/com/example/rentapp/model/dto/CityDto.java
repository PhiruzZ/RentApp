package com.example.rentapp.model.dto;

import com.example.rentapp.model.embedable.CityItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CityDto {

    private Long id;
    private String name;

    public static List<CityDto> listOf(List<CityItem> cities) {
        return cities.stream()
                .map(city->CityDto.builder()
                        .id(city.getCityId())
                        .name(city.getName())
                        .build())
                .toList();
    }
}
