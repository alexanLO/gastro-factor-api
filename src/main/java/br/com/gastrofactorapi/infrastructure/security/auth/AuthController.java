package br.com.gastrofactorapi.infrastructure.security.auth;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gastrofactorapi.infrastructure.exceptions.BusinessException;
import br.com.gastrofactorapi.infrastructure.security.JwtUtils;
import br.com.gastrofactorapi.infrastructure.security.auth.dto.AuthResponse;
import br.com.gastrofactorapi.infrastructure.security.auth.dto.LogoutRequest;
import br.com.gastrofactorapi.infrastructure.security.auth.entity.JwtBlacklistEntity;
import br.com.gastrofactorapi.infrastructure.security.auth.entity.RefreshTokenEntity;
import br.com.gastrofactorapi.infrastructure.security.auth.entity.UserEntity;
import br.com.gastrofactorapi.infrastructure.security.auth.enums.ProvidersEnum;
import br.com.gastrofactorapi.infrastructure.security.auth.repository.JwtBlacklistRepository;
import br.com.gastrofactorapi.infrastructure.security.auth.repository.RefreshTokenRepository;
import br.com.gastrofactorapi.infrastructure.security.auth.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserEntity entity) {
        log.info("Chamando requisição para o cadastro do usuario com nome = {} e email = {}", entity.getName(),
                entity.getEmail());

        userRepository.findByEmail(entity.getEmail())
                .ifPresent(user -> {
                    throw new BusinessException(
                            HttpStatus.CONFLICT.value(),
                            "Já existe um usuário com este email.");
                });

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setProvider(ProvidersEnum.LOCAL.name());
        entity.setRole("USER");

        userRepository.save(entity);

        AuthResponse response = new AuthResponse(jwtUtils.generateToken(entity), null);

        log.info("Usuário cadastrado com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserEntity entity) {
        log.info("Chamando requisição para logar usuario com email: {}", entity.getEmail());

        var user = userRepository.findByEmail(entity.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Email ou senha invalida."));

        boolean passwordMatches = passwordEncoder.matches(entity.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Email ou senha inválidos");
        }

        String token = jwtUtils.generateToken(user);

        RefreshTokenEntity refreshToken = this.createRefreshToken(user);

        AuthResponse response = new AuthResponse(token, refreshToken.getToken());

        log.info("Usuário logado com sucesso.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh/{token}")
    public ResponseEntity<AuthResponse> refresh(@PathVariable("token") String token) {
        log.info("Chamando requisição para atualizar o token");

        RefreshTokenEntity refreshTokenEntity = validate(token);

        UserEntity userEntity = refreshTokenEntity.getUser();

        String newAccessToken = jwtUtils.generateToken(userEntity);

        RefreshTokenEntity newRefresh = this.createRefreshToken(userEntity);

        this.revoke(refreshTokenEntity, newRefresh.getToken());

        AuthResponse response = new AuthResponse(newAccessToken, newRefresh.getToken());

        log.info("Token atualizado com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody LogoutRequest request) {
        log.info("Chamando requisição para logout.");

        if (authorization == null || authorization.isBlank() || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Access token inválido");
        }

        String accessToken = authorization.substring(7);
        String refreshToken = request.refreshToken();

        RefreshTokenEntity refreshTokenResponse = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Refresh token inválido"));

        refreshTokenResponse.setRevoked(true);

        refreshTokenRepository.save(refreshTokenResponse);

        LocalDateTime expiration = jwtUtils.extractExpiration(accessToken);

        JwtBlacklistEntity entity = JwtBlacklistEntity.builder()
            .token(accessToken)
                .expiresAt(expiration)
                .build();

        jwtBlacklistRepository.save(entity);

        log.info("Logout realizado com sucesso");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private RefreshTokenEntity createRefreshToken(UserEntity user) {

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshTokenEntity validate(String token) {

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Refresh token inválido"));

        if (refreshToken.isRevoked()) {
            if (refreshToken.getReplacedByToken().isBlank()) {
                log.warn("Tentativa de reutilização de refresh token");

                throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Refresh token revogado");
            }

            throw new BusinessException(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Refresh token revogado");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "Refresh token expirado");
        }

        return refreshToken;
    }

    private void revoke(RefreshTokenEntity refreshToken, String replacedByToken) {
        refreshToken.setRevoked(true);
        refreshToken.setReplacedByToken(replacedByToken);

        refreshTokenRepository.save(refreshToken);
    }
}
