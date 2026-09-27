package com.klaha.renthub.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "email не может быть пустым")
    @Email(message = "неверынй формат email")
    @Size(max = 64,message = "email слишком длинный ")
    private String email;
    @NotBlank(message = "пароль не может быть пустым")
    @Size(max=64,message = "пароль слишком длинный")
    private String password;
    @NotBlank(message = "заполните имя")
    @Size(max=40,message = "Имя слмшкмо длинное")
    private String firstName;
    @Size(max=40,message = "Фамилия слишком длинная")
    private String lastName;

}
