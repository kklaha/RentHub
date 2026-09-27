package com.klaha.renthub.auth.entity;


import com.klaha.renthub.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email",unique = true,nullable = false)
    private String email;

    @Column(name="password_hash",nullable = false)
    private String passwordHash;

    @Column(name="first_name",nullable = false,length = 64)
    private String fisrtName;

    @Column(name="last_name",nullable = false,length = 64)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false,length = 20)
    private Role role;

    @CreationTimestamp
    @Column(name="createdAt",nullable = false,updatable = false)
    private LocalDateTime createdAt;
}
