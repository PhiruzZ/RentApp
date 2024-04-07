package com.example.rentapp.model.embedable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
public class CityItem {

    private Long cityId;
    private String name;

    public CityItem(Long id, String name) {
        this.cityId = id;
        this.name = name;
    }
}
