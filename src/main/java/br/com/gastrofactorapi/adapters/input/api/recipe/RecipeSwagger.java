package br.com.gastrofactorapi.adapters.input.api.recipe;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.RecipeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

public interface RecipeSwagger {

    @Operation(summary = "Salvar Receitas", description = "Realiza o salvamento de receitas com base nas informações passadas pelo usuário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Receita salva com sucesso", content = @Content(schema = @Schema(implementation = UUID.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<UUID> save(@Valid @RequestBody RecipeRequest request);

    @Operation(summary = "Listar Receitas", description = "Realiza a listagem das receitas salvar na base.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de receitas retornadas com sucesso", content = @Content(schema = @Schema(implementation = RecipeRequest.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<RecipeRequest>> listAll();
}
