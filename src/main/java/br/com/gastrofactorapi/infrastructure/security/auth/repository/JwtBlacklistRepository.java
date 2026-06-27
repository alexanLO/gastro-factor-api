package br.com.gastrofactorapi.infrastructure.security.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gastrofactorapi.infrastructure.security.auth.entity.JwtBlacklistEntity;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklistEntity, UUID> {

    boolean existsByToken(String token);
}
