package br.com.gastrofactorapi.infrastructure.security.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String name,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @NotBlank(message = "Senha e obrigatoria")
        String password,

        String occupation) {
}