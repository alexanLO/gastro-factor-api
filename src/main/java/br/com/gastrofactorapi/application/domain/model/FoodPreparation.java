package br.com.gastrofactorapi.application.domain.model;

import java.math.BigDecimal;

public record FoodPreparation(String preparationType, BigDecimal correctionFactor, BigDecimal coccionFactor) {
}
