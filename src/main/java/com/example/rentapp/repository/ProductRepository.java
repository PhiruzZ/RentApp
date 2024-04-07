package com.example.rentapp.repository;

import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDbStatus(Long id, DbStatus dbStatus);
}
