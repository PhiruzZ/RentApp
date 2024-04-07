package com.example.rentapp.repository;

import com.example.rentapp.model.entity.City;
import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.FilterProductsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByIdAndDbStatus(Long id, DbStatus dbStatus);

    @Query("""
        select p
        from Product p
        where p.id = :#{#request.ge}
    """)
    List<Product> filter(FilterProductsRequest request);
}
