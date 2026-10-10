package com.klaha.renthub.auth.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access-token-ttl}")
    private long expirationMs;

    public String buildToken(Map<String,Object> extraClaims,
                             UserDetails userDetails,
                             long expiration){
        return Jwts.builder().claims(extraClaims).
                subject(userDetails.getUsername()).
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis()+expiration)).
                signWith(getSigningKey()).compact();
    }
    public String generateToken(UserDetails userDetails){
        Map<String,Object> extraClaims =new HashMap<>();
        extraClaims.put("role",userDetails.getAuthorities().stream().findFirst().
                map(GrantedAuthority::getAuthority).orElse("User"));
        return buildToken(extraClaims,userDetails,expirationMs);
    }
    public String generateToken(Map<String,Object> extraClaims,
                                UserDetails userDetails){
        return buildToken(extraClaims,userDetails,expirationMs);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser().
                verifyWith(getSigningKey()).
                build().
                parseSignedClaims(token).
                getPayload();
    }
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    public SecretKey getSigningKey(){
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
