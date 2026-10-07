package com.klaha.renthub.auth.service;


import com.klaha.renthub.auth.entity.RefreshToken;
import com.klaha.renthub.auth.repository.RefreshTokenRepository;
import com.klaha.renthub.exception.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
//TODO : add XSS defend withc cookies
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh.refresh-token-ttl")
    private Long refreshExpirationMs;

    public RefreshToken createRefreshToken(String userID){
        return RefreshToken.builder().token(UUID.randomUUID().toString()).
                userId(userID).expTime(refreshExpirationMs).build();
    }

    public void deleteByToken(String token){
        repository.deleteById(token);
    }
    public void deleteAllByUserId(String userId){
        repository.deleteByUserId(userId);
    }
    public RefreshToken rotateToken(String token){
        RefreshToken oldToken=repository.findByUseryId(token).
                orElseThrow(()->new RefreshTokenNotFoundException("Refresh token expired or not found"));

        repository.delete(oldToken);
        return createRefreshToken(oldToken.getUserId());
    }

}
