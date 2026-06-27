package br.com.gastrofactorapi.adapters.output.persistence.recipe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import br.com.gastrofactorapi.adapters.output.persistence.recipe.entity.DetailsEntity;
import br.com.gastrofactorapi.adapters.output.persistence.recipe.entity.IngredientEntity;
import br.com.gastrofactorapi.adapters.output.persistence.recipe.entity.NutritionalEntity;
import br.com.gastrofactorapi.adapters.output.persistence.recipe.entity.PreparationMethodEntity;
import br.com.gastrofactorapi.adapters.output.persistence.recipe.entity.RecipeEntity;
import br.com.gastrofactorapi.application.command.DetailsCommand;
import br.com.gastrofactorapi.application.command.IngredientCommand;
import br.com.gastrofactorapi.application.command.NutritionalCommand;
import br.com.gastrofactorapi.application.command.PreparationMethodCommand;
import br.com.gastrofactorapi.application.command.RecipeCommand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipePersistenceMapper {

    @Mapping(target = "id", ignore = true)
    RecipeEntity fromCommandToEntity(RecipeCommand command);

    List<RecipeCommand> fromEntityToCommand(List<RecipeEntity> entities);

    // auxiliares para mapear os tipos internos
    DetailsEntity toEntityDetails(DetailsCommand details);

    IngredientEntity toEntityIngredient(IngredientCommand ingredient);

    NutritionalEntity toEntityNutritional(NutritionalCommand nutritional);

    PreparationMethodEntity toEntityPreparationMethod(PreparationMethodCommand preparationMethod);

    // inverso
    DetailsCommand toCommandDetails(DetailsEntity details);

    IngredientCommand toCommandIngredient(IngredientEntity ingredient);

    NutritionalCommand toCommandNutritional(NutritionalEntity nutritional);

    PreparationMethodCommand toCommandPreparationMethod(PreparationMethodEntity preparationMethod);
}
