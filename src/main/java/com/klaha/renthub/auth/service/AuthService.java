package com.klaha.renthub.auth.service;


import com.klaha.renthub.auth.dto.*;
import com.klaha.renthub.auth.entity.RefreshToken;
import com.klaha.renthub.auth.entity.User;
import com.klaha.renthub.auth.repository.UserRepository;
import com.klaha.renthub.enums.Role;
import com.klaha.renthub.exception.AuthenticationCredentialsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository repository;
    private final AuthenticationManager manager;
    private final PasswordEncoder encoder;
    private final RefreshTokenService tokenService;
    private final CustomUserDetailsService userService;
    private final JwtService jwtService;

    @Transactional
    public AuthResult register(@NonNull RegisterRequestDto dto){
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существуют");
        }
        if (repository.existsByUsername(dto.getUsername())){
            throw new RuntimeException("Пользователь с таким никнейном уже существует");

        }
        String passwordHash=encoder.encode(dto.getPassword());
        User user =User.builder().email(dto.getEmail()).passwordHash(passwordHash)
                .username(dto.getUsername()).firstName(dto.getFirstName()).lastName(dto.getLastName()).role(Role.ROLE_USER).
                createdAt(OffsetDateTime.now()).build();
        User saved=repository.save(user);
        String accessToken=jwtService.generateToken(saved);
        RefreshToken refreshToken=tokenService.createRefreshToken(saved.getId().toString());
        AuthResponseDto response= AuthResponseDto.builder().accessToken(accessToken).
                id(saved.getId()).email(saved.getEmail()).
                username(saved.getUsername()).build();
        return new AuthResult(response,refreshToken.getToken());

    }
    public AuthResult login(@NonNull AuthRequestDto dto){
        manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
        User user=repository.findByEmail(dto.getEmail()).
                orElseThrow(()->new AuthenticationCredentialsException("Неверный email или пароль"));
        if(!user.isEnabled()){
            throw new DisabledException("Пользователь заблокирован");
        }
        String accessToken=jwtService.generateToken(user);
        RefreshToken refreshToken=tokenService.createRefreshToken(user.getId().toString());
        AuthResponseDto response= AuthResponseDto.builder().accessToken(accessToken).
                id(user.getId()).email(user.getEmail()).
                username(user.getUsername()).build();
        return new AuthResult(response,refreshToken.getToken());

    }
    public RefreshResult refresh(String oldRefreshToken){
        RefreshToken newToken=tokenService.rotateToken(oldRefreshToken);
        UserDetails userDetails=userService.loadUserByUsername(newToken.getUserId());
        if (!userDetails.isEnabled()) {
            throw new DisabledException("Пользователь заблокирован");
        }
        String newAccessToken=jwtService.generateToken(userDetails);
        TokenResponseDto response= TokenResponseDto.builder().accessToken(newAccessToken).build();
        return new RefreshResult(response,newToken.getToken());
    }
    public void logout(String refreshToken){
        tokenService.deleteByToken(refreshToken);
    }
    public void logoutAllDevice(String username){
        tokenService.deleteAllByUserId(username);
    }

}
