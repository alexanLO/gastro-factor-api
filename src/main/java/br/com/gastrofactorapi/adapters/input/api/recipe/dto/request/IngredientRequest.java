package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {

    @NotBlank(message = "Nome do ingrediente é obrigatório")
    private String name;
    private BigDecimal netWeight;
    private BigDecimal correctionFactor;
    private BigDecimal grossWeight;
    private BigDecimal cookingFactor;
    private BigDecimal totalQuantity;
}
