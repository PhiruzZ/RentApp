package com.example.rentapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_document")
public class UserDocument extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "document_type")
    private DocumentType documentType;

}
