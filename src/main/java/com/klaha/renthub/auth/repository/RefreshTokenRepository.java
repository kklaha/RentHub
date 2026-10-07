package com.klaha.renthub.auth.repository;

import com.klaha.renthub.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {

    Optional<RefreshToken> findByUseryId(String userId);
    void deleteByUserId(String username);

}
