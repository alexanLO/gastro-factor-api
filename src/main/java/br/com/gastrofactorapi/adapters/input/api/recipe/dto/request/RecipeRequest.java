package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {

    @Valid
    private DetailsRequest details;

    private List<@Valid IngredientRequest> ingredients;

    @Valid
    private NutritionalRequest nutritional;

    private List<@Valid PreparationMethodRequest> preparationMethods;
}
