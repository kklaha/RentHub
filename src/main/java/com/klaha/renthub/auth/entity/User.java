package com.klaha.renthub.auth.entity;


import com.klaha.renthub.enums.Role;
import com.klaha.renthub.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email",unique = true,nullable = false)
    private String email;

    @Column(name="password_hash",nullable = false)
    private String passwordHash;
    @Column(name="username",nullable=false,length = 32)
    private String username;
    @Column(name="first_name",nullable = false,length = 64)
    private String firstName;

    @Column(name="last_name",nullable = false,length = 64)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false,length = 20)
    private Role role=Role.ROLE_USER;
    @Builder.Default
    @Column(name="enabled",nullable = false)
    private boolean enabled=true;
    @CreationTimestamp
    @Column(name="createdAt",nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "userId",fetch = FetchType.LAZY)
    private List<Item> items=new ArrayList<>();
    @Override
    public @NonNull String getUsername(){
        return this.username;
    }
    @Override
    public @NonNull String getPassword(){
        return this.passwordHash;
    }
    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }
    @Override
    public boolean isEnabled(){
        return this.enabled;
    }


}
