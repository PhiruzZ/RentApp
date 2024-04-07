package com.example.rentapp.model.embedable;

import com.example.rentapp.model.request.CreatePropertyValueRequest;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class PropertyValue {

    private String value;

    public PropertyValue(CreatePropertyValueRequest request) {
        this.value = request.getValue();
    }
}
