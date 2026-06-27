package br.com.gastrofactorapi.application.domain.service;

import java.util.List;
import java.util.UUID;

import br.com.gastrofactorapi.application.command.RecipeCommand;
import br.com.gastrofactorapi.ports.input.RecipeUseCase;
import br.com.gastrofactorapi.ports.output.RecipePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RecipeService implements RecipeUseCase {

    private final RecipePort recipePort;

    @Override
    public UUID save(RecipeCommand command) {
        log.info("[RecipeService] Iniciando processamento para salvar a receita.");

        return recipePort.save(command);
    }

    @Override
    public List<RecipeCommand> listAll() {
        log.info("[RecipeService] Iniciando processamento para listar as receitas.");
        return recipePort.listAll();
    }
}
