package com.klaha.renthub.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> badCredentialsExceptionHandler(BadCredentialsException ex){
        Map<String,String> response=new HashMap<>();
        response.put("error","unauthorized");
        response.put("message","Неверный логин или пароль");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRefreshTokenNotFound
            (RefreshTokenNotFoundException ex){
               ErrorResponseDto error=ErrorResponseDto.builder().
                       status(HttpStatus.UNAUTHORIZED.value()).
                error("Unnauthorized").
                message(ex.getMessage()).
                timestamp(LocalDateTime.now()).build();
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
