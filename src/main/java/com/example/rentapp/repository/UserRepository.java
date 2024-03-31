package com.example.rentapp.repository;

import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAndDbStatus(String email, DbStatus status);

    Optional<UserEntity> findByIdAndDbStatus(Long id, DbStatus status);

}
