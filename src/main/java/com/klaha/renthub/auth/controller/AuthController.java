package com.klaha.renthub.auth.controller;


import com.klaha.renthub.auth.dto.AuthRequestDto;
import com.klaha.renthub.auth.dto.AuthResponseDto;
import com.klaha.renthub.auth.dto.RegisterRequestDto;
import com.klaha.renthub.auth.service.AuthService;
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
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){
        AuthResponseDto response=service.register(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto dto){
        AuthResponseDto response=service.login(dto);
        return ResponseEntity.ok(response);
    }

}
