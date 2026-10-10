package com.klaha.renthub.auth.dto;

public record RefreshResult(TokenResponseDto accessToken,String refreshToken){}
