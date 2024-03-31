package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.DocumentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class DocumentTypeDto {

    private Long id;
    private String name;

    public static List<DocumentTypeDto> listOf(List<DocumentType> documentTypes) {
        return documentTypes
                .stream()
                .map(DocumentTypeDto::of)
                .toList();
    }

    public static DocumentTypeDto of(DocumentType documentType) {
        return DocumentTypeDto.builder()
                .id(documentType.getId())
                .name(documentType.getName())
                .build();
    }
}
