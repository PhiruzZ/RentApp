package com.example.rentapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product")
public class Product extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "category")
    private ProductCategory category;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "address")
    private String address;



}
