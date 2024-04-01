package com.example.rentapp.model.entity;

import com.example.rentapp.model.embedable.PropertyValue;
import com.example.rentapp.model.enums.AgreementParticipants;
import com.example.rentapp.model.enums.PropertyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "category_requirement")
public class CategoryRequirement extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "category")
    private ProductCategory category;

    @Column(name = "property_name")
    private String propertyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PropertyType propertyType;

    @Column(name = "required")
    private Boolean required = false;

    @Column(name = "values_predefined")
    private Boolean valuesPredefined;

    @ElementCollection
    private List<PropertyValue> possibleValues;

}
