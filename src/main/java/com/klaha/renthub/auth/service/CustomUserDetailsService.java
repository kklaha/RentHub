package com.klaha.renthub.auth.service;

import com.klaha.renthub.auth.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException{
        System.out.println(" Ищу пользователя по email: " + email);
        UserDetails user=repository.findByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("Пользовательн не найден"));
        System.out.println("Пользователь найден: " + user.getUsername());
        return user;
    }


}
