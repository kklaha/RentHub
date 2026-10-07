package com.klaha.renthub.auth.service;


import com.klaha.renthub.auth.dto.AuthRequestDto;
import com.klaha.renthub.auth.dto.AuthResponseDto;
import com.klaha.renthub.auth.dto.RefreshTokenResponseDto;
import com.klaha.renthub.auth.dto.RegisterRequestDto;
import com.klaha.renthub.auth.entity.RefreshToken;
import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.auth.repository.AuthRepository;
import com.klaha.renthub.enums.Role;
import com.klaha.renthub.exception.AuthenticationCredentialsException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthRepository repository;
    private final AuthenticationManager manager;
    private final PasswordEncoder encoder;
    private final RefreshTokenService tokenService;
    private final UserDetailsService userService;
    private final JwtService jwtService;

    public AuthResponseDto register(@NonNull RegisterRequestDto dto){
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существуют");
        }
        if (repository.existsByUsername(dto.getUsername())){
            throw new RuntimeException("Пользователь с таким никнейном уже существует");

        }
        String passwordHash=encoder.encode(dto.getPassword());
        User user =User.builder().email(dto.getEmail()).passwordHash(passwordHash)
                .username(dto.getUsername()).lastName(dto.getLastName()).role(Role.ROLE_USER).
                createdAt(LocalDateTime.now()).build();
        User saved=repository.save(user);
        String accessToken=jwtService.generateToken(user);
        RefreshToken refreshToken=tokenService.createRefreshToken(user.getId().toString());
        return AuthResponseDto.builder().accessToken(accessToken).
                refreshToken(refreshToken.getToken()).
                id(saved.getId()).email(saved.getEmail()).
                username(saved.getUsername()).build();

    }
    public AuthResponseDto login(@NonNull AuthRequestDto dto){
        manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
        User user=repository.findByEmail(dto.getEmail()).
                orElseThrow(()->new AuthenticationCredentialsException("Пользователь с таким email не найден"));
        if(!user.isEnabled()){
            throw new DisabledException("Пользователь заблокирован");
        }
        String accessToken=jwtService.generateToken(user);
        RefreshToken refreshToken=tokenService.createRefreshToken(user.getId().toString());
        return AuthResponseDto.builder().accessToken(accessToken).
                refreshToken(refreshToken.getToken()).
                id(user.getId()).email(user.getEmail()).
                username(user.getUsername()).build();

    }
    public RefreshTokenResponseDto refresh(String oldRefreshToken){
        RefreshToken newToken=tokenService.rotateToken(oldRefreshToken);

        UserDetails userDetails=userService.loadUserByUsername(newToken.getUserId());
        String newAccessToken=jwtService.generateToken(userDetails);
        return RefreshTokenResponseDto.builder().
                accessToken(newAccessToken).
                refreshToken(newToken.getToken()).
                build();
    }
    public void logout(String refreshToken){
        tokenService.deleteByToken(refreshToken);
    }
    public void logoutAllDevice(String username){
        tokenService.deleteAllByUserId(username);
    }

}
