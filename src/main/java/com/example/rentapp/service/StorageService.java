package com.example.rentapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StorageService {
    public String uploadDocument(MultipartFile document) {
        return "Mock address";
    }
}
