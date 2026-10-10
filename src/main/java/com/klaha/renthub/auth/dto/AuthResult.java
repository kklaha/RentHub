package com.klaha.renthub.auth.dto;

public record AuthResult(AuthResponseDto response, String refreshToken){}
