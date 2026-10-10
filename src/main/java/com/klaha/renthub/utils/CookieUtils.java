package com.klaha.renthub.utils;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtils {

    @Value("${cookie.refresh-token.name}")
    private String refreshTokenName;
    @Value("${cookie.refresh-token.max-age-days}")
    private long maxAgeDays;
    @Value("${cookie.refresh-token.path}")
    private String path;
    @Value("${cookie.refresh-token.secure}")
    private boolean secure;

    public ResponseCookie createRefreshTokenCookie(String token){
        return ResponseCookie.from(refreshTokenName,token).
                httpOnly(true).
                secure(secure).sameSite("Strict").
                maxAge(Duration.ofDays(maxAgeDays)).
                path(path).build();

    }
    public ResponseCookie logoutRefreshTokenCookie(){
        return ResponseCookie.from(refreshTokenName,"").httpOnly(true).
                sameSite("Strict").secure(secure).maxAge(Duration.ZERO).path(path).build();
    }
    public Optional<String> extractRefreshTokenFromCookie(HttpServletRequest request){
        if(request.getCookies()==null){
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies()).
                filter(cookie->refreshTokenName.equals(cookie.getName())).
                map(Cookie::getValue).findFirst();
    }
    public String getCookieName(){
        return refreshTokenName;
    }

}
