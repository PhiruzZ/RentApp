package com.example.rentapp.repository;

import com.example.rentapp.model.entity.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDocumentRepository extends JpaRepository<UserDocument, Long> {
}
