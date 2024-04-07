package com.example.rentapp.repository;

import com.example.rentapp.model.entity.ProductProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPropertiesRepository extends JpaRepository<ProductProperties, Long> {
}
