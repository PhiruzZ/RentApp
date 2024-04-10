package com.example.rentapp.repository;

import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.UserStatus;
import com.example.rentapp.model.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAndDbStatus(String email, DbStatus status);

    Optional<UserEntity> findByIdAndDbStatus(Long id, DbStatus status);

    Optional<UserEntity> findByIdAndDbStatusAndUserStatus(Long id, DbStatus dbStatus, UserStatus userStatus);

    List<UserEntity> findByVerificationStatusAndDbStatus(VerificationStatus verificationStatus, DbStatus dbStatus);
}
