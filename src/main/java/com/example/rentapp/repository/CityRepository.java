package com.example.rentapp.repository;

import com.example.rentapp.model.entity.City;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByIdAndDbStatus(Long id, DbStatus dbStatus);

}
