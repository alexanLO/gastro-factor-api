package br.com.gastrofactorapi.application.command;

import java.util.List;

import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.DetailsRequest;
import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.IngredientRequest;
import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.NutritionalRequest;
import br.com.gastrofactorapi.adapters.input.api.recipe.dto.request.PreparationMethodRequest;

public record RecipeCommand(DetailsRequest details, List<IngredientRequest> ingredients, NutritionalRequest nutritional,
        List<PreparationMethodRequest> preparationMethods) {

}