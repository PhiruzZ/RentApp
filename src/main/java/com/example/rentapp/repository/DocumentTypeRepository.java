package com.example.rentapp.repository;

import com.example.rentapp.model.entity.DocumentType;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    Optional<DocumentType> findByIdAndDbStatus(Long id, DbStatus dbStatus);

    List<DocumentType> findByDbStatus(DbStatus dbStatus);

    List<DocumentType> findByDbStatusAndAllowedForUserVerification(DbStatus dbStatus, Boolean allowed);
}
