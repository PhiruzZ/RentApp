package com.example.rentapp.model.embedable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class PropertyValue {

    private String value;

}
