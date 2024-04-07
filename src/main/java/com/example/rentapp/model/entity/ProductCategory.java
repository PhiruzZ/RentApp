package com.example.rentapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "product_category")
public class ProductCategory extends BaseEntity{

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<CategoryRequirement> requirements;

}
