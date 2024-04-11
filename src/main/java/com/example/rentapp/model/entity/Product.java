package com.example.rentapp.model.entity;

import com.example.rentapp.model.embedable.CityItem;
import com.example.rentapp.model.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "product")
public class Product extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "category")
    private ProductCategory category;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private UserEntity owner;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "price_id")
    private ProductPrice productPrice;

    @ElementCollection
    private List<CityItem> cities;

    @Column(name = "address")
    private String address;

    @Column(name = "available_from")
    private LocalDate availableFrom;

    @Column(name = "available_until")
    private LocalDate availableUntil;

    @Column(name = "advance_payment_percent")
    private Double advancePaymentPercent;

    @Column(name = "product_status")
    private ProductStatus productStatus;

    @OneToMany(mappedBy = "product")
    private List<ProductProperties> properties;

}
