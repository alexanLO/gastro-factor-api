package br.com.gastrofactorapi.application.domain.model;

import java.math.BigDecimal;

public record FoodNutrition(String portion, BigDecimal calories, BigDecimal protein, BigDecimal carbohydrate,
        BigDecimal fat) {
}
