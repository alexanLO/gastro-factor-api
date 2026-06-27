package br.com.gastrofactorapi.infrastructure.security.auth.dto;

public record AuthResponse(String accessToken, String refreshToken) {
}