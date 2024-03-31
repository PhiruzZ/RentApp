package com.example.rentapp.service;

import com.example.rentapp.model.entity.DocumentType;
import com.example.rentapp.model.entity.UserDocument;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.repository.UserDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserDocumentService {

    private final UserDocumentRepository userDocumentRepository;
    private final StorageService storageService;


    @Transactional
    public void create(UserEntity user, MultipartFile document,  DocumentType documentType){
        UserDocument userDocument = new UserDocument();
        String documentAddress = storageService.uploadDocument(document);
        userDocument.setUser(user);
        userDocument.setAddress(documentAddress);
        userDocument.setDocumentType(documentType);
        userDocumentRepository.save(userDocument);
    }

}
