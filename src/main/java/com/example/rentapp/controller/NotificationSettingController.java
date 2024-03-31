package com.example.rentapp.controller;

import com.example.rentapp.model.dto.NotificationSettingDto;
import com.example.rentapp.model.request.CreateNotificationSettingRequest;
import com.example.rentapp.service.NotificationSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification/setting")
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;

    @PostMapping
    public ResponseEntity<Void> addSetting(@RequestBody @Valid CreateNotificationSettingRequest request){
        notificationSettingService.addSetting(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeSetting(@RequestParam Long id){
        notificationSettingService.removeSetting(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationSettingDto>> getAll(){
        return ResponseEntity.ok(notificationSettingService.getAll());
    }

}
