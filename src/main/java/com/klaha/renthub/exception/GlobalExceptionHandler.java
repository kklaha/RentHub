package com.klaha.renthub.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> badCredentialsExceptionHandler(AuthenticationCredentialsException ex){
        Map<String,String> response=new HashMap<>();
        response.put("error","unauthorized");
        response.put("message","Неверный логин или пароль");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
