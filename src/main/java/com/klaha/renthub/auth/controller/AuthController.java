package com.klaha.renthub.auth.controller;


import com.klaha.renthub.auth.dto.*;
import com.klaha.renthub.auth.service.AuthService;
import com.klaha.renthub.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final CookieUtils utils;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid@RequestBody RegisterRequestDto dto,
                                                    HttpServletResponse response){
        AuthResult result=service.register(dto);
        response.addHeader(HttpHeaders.SET_COOKIE,
                utils.createRefreshTokenCookie(result.refreshToken()).toString());
        return ResponseEntity.ok(result.response());
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid@RequestBody AuthRequestDto dto,
                                                 HttpServletResponse response){

        AuthResult result=service.login(dto);
        response.addHeader(HttpHeaders.SET_COOKIE,utils.
                createRefreshTokenCookie(result.refreshToken()).toString());
        return ResponseEntity.ok(result.response());
    }
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(HttpServletRequest request,
                                                    HttpServletResponse response){
        String oldToken=utils.extractRefreshTokenFromCookie(request).orElseThrow(()->
                new RuntimeException("Refresh token not found"));
       RefreshResult result=service.refresh(oldToken);
       response.addHeader(HttpHeaders.SET_COOKIE,utils.createRefreshTokenCookie(result.refreshToken()).toString());
       return ResponseEntity.ok(result.accessToken());
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,HttpServletResponse response){
        utils.extractRefreshTokenFromCookie(request).ifPresent(service::logout);
        response.addHeader(HttpHeaders.SET_COOKIE,utils.logoutRefreshTokenCookie().toString());
        return ResponseEntity.ok().build();
    }

}
