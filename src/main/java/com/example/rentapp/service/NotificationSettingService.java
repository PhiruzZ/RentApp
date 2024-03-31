package com.example.rentapp.service;

import com.example.rentapp.model.dto.NotificationSettingDto;
import com.example.rentapp.model.entity.NotificationSetting;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.CreateNotificationSettingRequest;
import com.example.rentapp.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

    private final NotificationSettingRepository notificationSettingRepository;
    private final AuthService authService;

    @Transactional
    public void addSetting(CreateNotificationSettingRequest request) {
        UserEntity loggedUser = authService.getLoggedInUser();
        Optional<NotificationSetting> optionalSetting = notificationSettingRepository.findByUserIdAndNotificationChannelAndDbStatus(loggedUser.getId(), request.getNotificationChannel(), DbStatus.ACTIVE);
        if(optionalSetting.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Channel already exists for user");
        }
        NotificationSetting newSetting = NotificationSetting.of(request, loggedUser);
        notificationSettingRepository.save(newSetting);
    }

    @Transactional
    public void removeSetting(Long id) {
        UserEntity loggedUser = authService.getLoggedInUser();
        NotificationSetting setting = notificationSettingRepository.findByIdAndUserIdAndDbStatus(id, loggedUser.getId(), DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found active setting!"));
        setting.setDbStatus(DbStatus.DELETED);
        notificationSettingRepository.save(setting);
    }

    public List<NotificationSettingDto> getAll() {
        UserEntity loggedUser = authService.getLoggedInUser();
        List<NotificationSetting> notificationSettings = notificationSettingRepository.findByUserIdAndDbStatus(loggedUser.getId(), DbStatus.ACTIVE);
        return NotificationSettingDto.listOf(notificationSettings);
    }
}
