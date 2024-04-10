package com.example.rentapp.service;

import com.example.rentapp.model.dto.UserDocumentDto;
import com.example.rentapp.model.entity.DocumentType;
import com.example.rentapp.model.entity.UserDocument;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.repository.DocumentTypeRepository;
import com.example.rentapp.repository.UserDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDocumentService {

    private final UserDocumentRepository userDocumentRepository;
    private final StorageService storageService;
    private final AuthService authService;
    private final DocumentTypeRepository documentTypeRepository;


    @Transactional
    public void create(UserEntity user, MultipartFile document,  DocumentType documentType){
        UserDocument userDocument = new UserDocument();
        String documentAddress = storageService.uploadDocument(document);
        userDocument.setUser(user);
        userDocument.setAddress(documentAddress);
        userDocument.setDocumentType(documentType);
        userDocumentRepository.save(userDocument);
    }

    @Transactional
    public void addDocument(Long documentTypeId, MultipartFile document) {
        UserEntity user = authService.getLoggedInUser();
        Optional<UserDocument> existingDocument = userDocumentRepository.findByUserIdAndDocumentTypeIdAndDbStatus(user.getId(), documentTypeId, DbStatus.ACTIVE);
        if(existingDocument.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Document is already uploaded!");
        }
        DocumentType documentType = documentTypeRepository.findByIdAndDbStatus(documentTypeId, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "MNot found document type"));
        create(user, document, documentType);
    }

    @Transactional
    public void deleteDocument(Long id) {
        UserEntity user = authService.getLoggedInUser();
        Optional<UserDocument> existingDocument = userDocumentRepository.findByUserIdAndIdAndDbStatus(user.getId(), id, DbStatus.ACTIVE);
        if(existingDocument.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found!");
        }
        existingDocument.get().setDbStatus(DbStatus.DELETED);
        storageService.deleteDocument(existingDocument.get().getAddress());
        userDocumentRepository.save(existingDocument.get());
    }

    public UserDocument findByUserIdAndIdAndDbStatus(Long userId, Long userDocumentId, DbStatus dbStatus) {
        return userDocumentRepository.findByUserIdAndIdAndDbStatus(userId, userDocumentId, dbStatus)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found document"));
    }

    public List<UserDocumentDto> getAll() {
        UserEntity user = authService.getLoggedInUser();
        List<UserDocument> userDocuments = userDocumentRepository.findByUserIdAndDbStatus(user.getId(), DbStatus.ACTIVE);
        return UserDocumentDto.listOf(userDocuments);
    }

    public List<UserDocumentDto> getByUserId(Long userId) {
        List<UserDocument> userDocuments = userDocumentRepository.findByUserIdAndDbStatus(userId, DbStatus.ACTIVE);
        return UserDocumentDto.listOf(userDocuments);
    }
}
