package com.klaha.renthub.auth.repository;

import com.klaha.renthub.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail( String email);
    boolean existsByEmail(String email);
}
