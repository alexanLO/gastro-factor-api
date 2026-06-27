package br.com.gastrofactorapi.adapters.input.api.recipe;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.DetailsRequest;
import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.IngredientRequest;
import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.NutritionalRequest;
import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.PreparationMethodRequest;
import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.RecipeRequest;
import br.com.gastrofactorapi.application.command.DetailsCommand;
import br.com.gastrofactorapi.application.command.IngredientCommand;
import br.com.gastrofactorapi.application.command.NutritionalCommand;
import br.com.gastrofactorapi.application.command.PreparationMethodCommand;
import br.com.gastrofactorapi.application.command.RecipeCommand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecipeApiMapper {

    RecipeCommand toCommandRecipe(RecipeRequest request);

    List<RecipeRequest> toListRequestRecipe(List<RecipeCommand> command);

    // auxiliares para os tipos internos
    DetailsRequest toRequestDetails(DetailsCommand details);

    IngredientRequest toRequestIngredient(IngredientCommand ingredient);

    NutritionalRequest toRequestNutritional(NutritionalCommand nutritional);

    PreparationMethodRequest toRequestPreparationMethod(PreparationMethodCommand preparationMethod);

    DetailsCommand toCommandDetails(DetailsRequest details);

    IngredientCommand toCommandIngredient(IngredientRequest ingredient);

    NutritionalCommand toCommandNutritional(NutritionalRequest nutritional);

    PreparationMethodCommand toCommandPreparationMethod(PreparationMethodRequest preparationMethod);

}
