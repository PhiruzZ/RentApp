package com.example.rentapp.model.entity;

import com.example.rentapp.model.enums.AgreementParticipants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "category_requirement")
public class CategoryRequirement extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "category")
    private ProductCategory category;

    @ManyToOne
    @JoinColumn(name = "document_type")
    private DocumentType documentType;

    @Column(name = "required_for")
    @Enumerated(EnumType.STRING)
    private AgreementParticipants requiredFor;

}
