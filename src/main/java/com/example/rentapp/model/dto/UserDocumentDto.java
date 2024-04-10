package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.UserDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class UserDocumentDto {

    private Long id;
    private DocumentTypeDto documentType;

    public static List<UserDocumentDto> listOf(List<UserDocument> userDocuments) {
        return userDocuments.stream()
                .map(userDocument -> UserDocumentDto.builder()
                                .id(userDocument.getId())
                                .documentType(DocumentTypeDto.of(userDocument.getDocumentType()))
                                .build()
                ).toList();
    }
}
