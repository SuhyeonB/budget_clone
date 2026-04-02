package com.example.budget_clone.domain.auth.repository;

import com.example.budget_clone.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional <RefreshToken> findByUserId(Long userId);
}
