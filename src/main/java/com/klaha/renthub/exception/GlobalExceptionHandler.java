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
    @ExceptionHandler(ItemNotActiveException.class)
    public ResponseEntity<ErrorResponseDto> handleItemNotActiveException
            (ItemNotActiveException ex){
        ErrorResponseDto error= ErrorResponseDto.builder().
                status(HttpStatus.BAD_REQUEST.value()).
                error("Item is inactive").message(ex.getMessage()).
                timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(StatusException.class)
    public ResponseEntity<ErrorResponseDto> handleStatusExcpetion
            (StatusException ex){
        ErrorResponseDto error=ErrorResponseDto.builder().
                status(HttpStatus.METHOD_NOT_ALLOWED.value()).
                error("Status can't be moved to this").
                message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBussinesException(BusinessException ex){
        ErrorResponseDto error = ErrorResponseDto.builder().status(HttpStatus.BAD_REQUEST.value()).
                error("Item already ordered").message(ex.getMessage()).
                timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
