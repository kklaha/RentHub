package com.klaha.renthub.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthRequestDto {
    @NotBlank(message ="email не может быть пустым" )
    @Email(message = "Некорректный формат email")
    private String email;
    @NotBlank(message = "пароль не может быть пустым")
    private String password;

}
