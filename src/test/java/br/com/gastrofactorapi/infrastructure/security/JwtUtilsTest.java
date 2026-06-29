package br.com.gastrofactorapi.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import br.com.gastrofactorapi.infrastructure.security.auth.entity.UserEntity;

class JwtUtilsTest {

    private static final String VALID_SECRET = "12345678901234567890123456789012";

    @Test
    void shouldGenerateAndValidateToken() {
        JwtUtils jwtUtils = new JwtUtils(VALID_SECRET);
        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");
        user.setRole("USER");
        user.setProvider("LOCAL");

        String token = jwtUtils.generateToken(user);

        assertThat(token).isNotBlank();
        assertThat(jwtUtils.extractEmail(token)).isEqualTo("user@test.com");
        assertThat(jwtUtils.isTokenValid(token, "user@test.com")).isTrue();
    }

    @Test
    void shouldFailWhenSecretIsBlank() {
        assertThatThrownBy(() -> new JwtUtils("   "))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("nao pode ser vazio");
    }

    @Test
    void shouldFailWhenSecretIsTooShort() {
        assertThatThrownBy(() -> new JwtUtils("short-secret"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no minimo 32 bytes");
    }
}