package br.com.gastrofactorapi.adapters.input.api.recipe;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.RecipeRequest;
import br.com.gastrofactorapi.ports.input.RecipeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recipes")
public class RecipeController implements RecipeSwagger {

    private final RecipeUseCase recipeUseCase;
    private final RecipeApiMapper recipeApiMapper;

    @Override
    @PostMapping
    public ResponseEntity<UUID> save(@Valid RecipeRequest request) {
        log.info("[RecipeController] Iniciando chamada para slavar receita.");

        recipeUseCase.save(recipeApiMapper.toCommandRecipe(request));

        log.info("[RecipeController] Receita salva com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RecipeRequest>> listAll() {
        log.info("[RecipeController] Iniciando chamada de listagem de receitas.");

        List<RecipeRequest> recipes = recipeApiMapper.toListRequestRecipe(recipeUseCase.listAll());

        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

}
