package com.example.rentapp.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "agreement_request")
public class AgreementRequest extends BaseEntity{
}
