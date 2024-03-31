package com.example.rentapp.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "searched_product-subscription")
public class SearchedProductSubscription extends BaseEntity{
}
