package com.klaha.renthub.auth.service;


import com.klaha.renthub.auth.entity.RefreshToken;
import com.klaha.renthub.auth.repository.RefreshTokenRepository;
import com.klaha.renthub.exception.RefreshTokenNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy ;

import java.util.UUID;
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    public RefreshTokenService(@Lazy RefreshTokenRepository repository) {
        this.repository = repository;
    }


    @Value("${cookie.refresh-token.max-age-days}")
    private Long maxAgeDays;

    public RefreshToken createRefreshToken(String userID){
        long expirationInSeconds = maxAgeDays * 24L * 60L * 60L;
        return RefreshToken.builder().token(UUID.randomUUID().toString()).
                userId(userID).expTime(expirationInSeconds).build();
    }

    public void deleteByToken(String token){
        repository.deleteById(token);
    }
    public void deleteAllByUserId(String userId){
        repository.deleteByUserId(userId);
    }
    public RefreshToken rotateToken(String token){
        RefreshToken oldToken=repository.findByToken(token).
                orElseThrow(()->new RefreshTokenNotFoundException("Refresh token expired or not found"));

        repository.delete(oldToken);
        return createRefreshToken(oldToken.getUserId());
    }

}
