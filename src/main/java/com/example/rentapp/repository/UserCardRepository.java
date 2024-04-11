package com.example.rentapp.repository;

import com.example.rentapp.model.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {
}
