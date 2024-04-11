package com.example.rentapp.repository;

import com.example.rentapp.model.entity.UserDocument;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDocumentRepository extends JpaRepository<UserDocument, Long> {
    Optional<UserDocument> findByUserIdAndDocumentTypeIdAndDbStatus(Long id, Long documentTypeId, DbStatus dbStatus);

    Optional<UserDocument> findByUserIdAndIdAndDbStatus(Long id, Long id1, DbStatus dbStatus);

    List<UserDocument> findByUserIdAndDbStatus(Long id, DbStatus dbStatus);

    Optional<UserDocument> findByIdAndDbStatus(Long id, DbStatus dbStatus);
}
