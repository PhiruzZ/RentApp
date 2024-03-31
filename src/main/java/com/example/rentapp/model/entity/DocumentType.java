package com.example.rentapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "document_type")
public class DocumentType extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "allowed_for_user_verification")
    private boolean allowedForUserVerification = false;

}
