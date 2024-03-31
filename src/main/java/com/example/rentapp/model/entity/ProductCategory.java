package com.example.rentapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product_category")
public class ProductCategory extends BaseEntity{

    @Column(name = "name")
    private String name;

}
