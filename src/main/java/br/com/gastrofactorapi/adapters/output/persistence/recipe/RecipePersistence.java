package br.com.gastrofactorapi.adapters.output.persistence.recipe;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.gastrofactorapi.adapters.output.persistence.recipe.entity.RecipeEntity;
import br.com.gastrofactorapi.adapters.output.persistence.recipe.mapper.RecipePersistenceMapper;
import br.com.gastrofactorapi.adapters.output.persistence.recipe.repository.RecipeRepository;
import br.com.gastrofactorapi.application.command.RecipeCommand;
import br.com.gastrofactorapi.ports.output.RecipePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecipePersistence implements RecipePort {

    private final RecipeRepository recipeRepository;
    private final RecipePersistenceMapper recipePersistenceMapper;

    @Override
    public UUID save(RecipeCommand command) {
        log.info("[RecipePersistence] Iniciando o salvamento da receita no banco de dados.");

        RecipeEntity entity = recipePersistenceMapper.fromCommandToEntity(command);

        RecipeEntity entitySave = recipeRepository.save(entity);

        log.info("[RecipePersistence] Receita salva com sucesso no banco de dados.");
        return entitySave.getId();
    }

    @Override
    @Transactional
    public List<RecipeCommand> listAll() {
        log.info("[RecipePersistence] Iniciando a listagem das receita no banco de dados.");

        List<RecipeEntity> entity = recipeRepository.findAll();

        log.info("[RecipePersistence] Listagem das receita no banco de dados com sucesso.");
        return recipePersistenceMapper.fromEntityToCommand(entity);
    }

}
