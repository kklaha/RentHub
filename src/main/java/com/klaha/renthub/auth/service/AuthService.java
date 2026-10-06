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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//TODO: refactor this with following updates in architecture
@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthRepository repository;
    private final PasswordEncoder encoder;
    private final RefreshTokenService tokenService;
    private final UserDetailsService userService;
    private final JwtService jwtService;

    public AuthResponseDto register(RegisterRequestDto dto){
        String passwordHash=encoder.encode(dto.getPassword());
        User user =User.builder().email(dto.getEmail()).passwordHash(passwordHash)
                .username(dto.getUsername()).lastName(dto.getLastName()).role(Role.ROLE_USER).
                createdAt(LocalDateTime.now()).build();
        User saved=repository.save(user);
        return AuthResponseDto.builder().id(saved.getId()).email(saved.getEmail()).
                username(saved.getUsername()).build();

    }
    public AuthResponseDto login(AuthRequestDto dto){
        User user=repository.findByEmail(dto.getEmail()).
                orElseThrow(()->new AuthenticationCredentialsException("Пользователь с таким email не найден"));
        if(!encoder.matches(dto.getPassword(),user.getPasswordHash())){
            throw new AuthenticationCredentialsException("Неверный пароль");
        }
        return AuthResponseDto.builder().id(user.getId()).email(dto.getEmail()).
                username(user.getUsername()).build();

    }
    public RefreshTokenResponseDto refreshAccessToken(String oldRefreshToken){
        RefreshToken newToken=tokenService.rotateToken(oldRefreshToken);

        UserDetails userDetails=userService.loadUserByUsername(newToken.getUserId());
        String newAccessToken=jwtService.generateToken(userDetails);
        return RefreshTokenResponseDto.builder().
                accessToken(newAccessToken).
                refreshToken(newToken.getToken()).
                build();
    }


}
