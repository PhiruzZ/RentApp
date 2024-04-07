package com.example.rentapp.model.request;

import com.example.rentapp.model.embedable.PropertyValue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PredefinedValueRequest {

    private Long requirementId;
    private List<PropertyValue> values;

}
