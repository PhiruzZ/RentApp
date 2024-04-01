package com.example.rentapp.model.entity;

import com.example.rentapp.model.enums.TimeUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product_price")
public class ProductPrice extends BaseEntity{


    @Column(name = "basicPrice")
    private Double basicPrice;

    @Column(name = "time_unit_for_price")
    private TimeUnit timeUnitForPrice;

}
