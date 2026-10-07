package com.klaha.renthub.auth.controller;


import com.klaha.renthub.auth.dto.*;
import com.klaha.renthub.auth.entity.RefreshToken;
import com.klaha.renthub.auth.service.AuthService;
import com.klaha.renthub.auth.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid@RequestBody RegisterRequestDto dto){
        AuthResponseDto response=service.register(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid@RequestBody AuthRequestDto dto){

        AuthResponseDto response=service.login(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@Valid@RequestBody RefreshTokenRequestDto dto){
        return ResponseEntity.ok(service.refresh(dto.getRefreshToken()));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid@RequestBody RefreshTokenRequestDto dto){
        service.logout(dto.getRefreshToken());
        return ResponseEntity.ok().build();
    }

}
