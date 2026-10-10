package com.klaha.renthub.auth.dto;


import jakarta.validation.Valid;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponseDto {


    private String accessToken;
    private Long id;
    private String email;
    private  String username;


}
