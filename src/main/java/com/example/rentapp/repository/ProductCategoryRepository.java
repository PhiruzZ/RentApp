package com.example.rentapp.repository;

import com.example.rentapp.model.entity.ProductCategory;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByIdAndDbStatus(Long id, DbStatus dbStatus);

    List<ProductCategory> findByDbStatus(DbStatus dbStatus);
}
