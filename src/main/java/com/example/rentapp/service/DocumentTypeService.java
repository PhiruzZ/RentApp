package com.example.rentapp.service;

import com.example.rentapp.model.dto.DocumentTypeDto;
import com.example.rentapp.model.entity.DocumentType;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.CreateDocumentTypeRequest;
import com.example.rentapp.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public void addDocumentType(CreateDocumentTypeRequest request) {
        DocumentType documentType = new DocumentType();
        documentType.setName(request.getName());
        documentType.setAllowedForUserVerification(request.getAllowedForUserVerification());
        documentTypeRepository.save(documentType);
    }

    @Transactional
    public void delete(Long id) {
        DocumentType documentType = getById(id);
        documentType.setDbStatus(DbStatus.DELETED);
        documentTypeRepository.save(documentType);
    }

    public DocumentType getById(Long id) {
        return documentTypeRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found document type!"));
    }

    public List<DocumentTypeDto> getAll() {
        List<DocumentType> documentTypes = documentTypeRepository.findByDbStatus(DbStatus.ACTIVE);
        return DocumentTypeDto.listOf(documentTypes);
    }

    public List<DocumentTypeDto> getForVerification() {
        List<DocumentType> documentTypes = documentTypeRepository.findByDbStatusAndAllowedForUserVerification(DbStatus.ACTIVE, true);
        return DocumentTypeDto.listOf(documentTypes);
    }
}
