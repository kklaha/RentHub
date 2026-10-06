package com.klaha.renthub.auth.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDto {

    private String accessToken;
    private String refreshToken;
}
