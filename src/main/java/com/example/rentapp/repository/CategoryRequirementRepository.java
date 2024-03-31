package com.example.rentapp.repository;

import com.example.rentapp.model.entity.CategoryRequirement;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRequirementRepository extends JpaRepository<CategoryRequirement, Long> {
    Optional<CategoryRequirement> findByIdAndDbStatus(Long id, DbStatus dbStatus);

    List<CategoryRequirement> findByCategoryIdAndDbStatus(Long categoryId, DbStatus dbStatus);
}
