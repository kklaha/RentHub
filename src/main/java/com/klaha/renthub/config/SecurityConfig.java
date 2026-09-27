package com.klaha.renthub.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


   /* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.authorizeHttpRequests(auth->
                auth.requestMatchers("api/v1/login").permitAll().
                        requestMatchers("apit/v1/register").permitAll()).
                sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                csrf(AbstractHttpConfigurer::disable).addFilterBefore();
        return http.build();

    }

    */
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder(15);
    }
}
