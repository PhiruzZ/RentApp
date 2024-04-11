package com.example.rentapp.model.request;

import com.example.rentapp.model.embedable.PropertyValue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PredefinedValueRequest {

    @NotNull
    private Long requirementId;
    @NotEmpty
    private List<PropertyValue> values;

}
