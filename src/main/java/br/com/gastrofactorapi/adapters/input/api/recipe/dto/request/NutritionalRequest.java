package br.com.gastrofactorapi.adapters.input.api.recipe.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionalRequest {
    
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal totalFat;
    private BigDecimal carbs;
}
