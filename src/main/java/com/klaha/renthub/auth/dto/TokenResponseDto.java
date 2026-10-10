package com.klaha.renthub.auth.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;
}
