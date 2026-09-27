package com.klaha.renthub.auth.dto;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponseDto {


    private String token;
    private Long id;
    private String email;
    private  String username;


}
