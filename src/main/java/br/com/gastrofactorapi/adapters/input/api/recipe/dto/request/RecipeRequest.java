package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {

    private DetailsRequest details;

    private List<IngredientRequest> ingredients;

    private NutritionalRequest nutritional;

    private List<PreparationMethodRequest> preparationMethods;
}
