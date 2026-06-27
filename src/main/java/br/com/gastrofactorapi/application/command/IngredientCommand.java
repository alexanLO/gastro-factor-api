package br.com.gastrofactorapi.application.command;

import java.math.BigDecimal;

public record IngredientCommand(String name, BigDecimal netWeight, BigDecimal correctionFactor, BigDecimal grossWeight,
        BigDecimal cookingFactor, BigDecimal totalQuantity) {

}
