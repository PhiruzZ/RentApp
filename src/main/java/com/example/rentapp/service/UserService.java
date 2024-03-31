package com.example.rentapp.service;

import com.example.rentapp.model.entity.DocumentType;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.UserStatus;
import com.example.rentapp.model.enums.VerificationStatus;
import com.example.rentapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserDocumentService userDocumentService;
    private final DocumentTypeService documentTypeService;

    @Transactional
    public void blockUser(Long id) {
        UserEntity user = findById(id);
        if(user.getUserStatus().equals(UserStatus.BLOCKED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already blocked!");
        }
        user.setUserStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    @Transactional
    public void unblockUser(Long id) {
        UserEntity user = findById(id);
        if(user.getUserStatus().equals(UserStatus.ACTIVE)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already unblocked!");
        }
        user.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Transactional
    public void requestVerification(MultipartFile document, Long documentTypeId) {
        UserEntity user = authService.getLoggedInUser();
        if(!user.getVerificationStatus().equals(VerificationStatus.NOT_VERIFIED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Verification is already requested!");
        }
        DocumentType documentType = documentTypeService.getById(documentTypeId);
        if(!documentType.isAllowedForUserVerification()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid document for verification!");
        }
        user.setVerificationStatus(VerificationStatus.PENDING);
        userDocumentService.create(user, document, documentType);
        userRepository.save(user);
    }

    @Transactional
    public void confirmVerification(Long id) {
        UserEntity user = findById(id);
        if(!user.getVerificationStatus().equals(VerificationStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has not requested verification!");
        }
        user.setVerificationStatus(VerificationStatus.VERIFIED);
        userRepository.save(user);
    }

    private UserEntity findById(Long id) {
        return userRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user!"));
    }

    public void rejectVerification(Long id) {
        UserEntity user = findById(id);
        if(!user.getVerificationStatus().equals(VerificationStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has not requested verification!");
        }
        user.setVerificationStatus(VerificationStatus.REJECTED);
        userRepository.save(user);
    }
}
