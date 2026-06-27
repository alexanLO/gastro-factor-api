package br.com.gastrofactorapi.application.command;

import java.math.BigDecimal;

public record NutritionalCommand(BigDecimal calories, BigDecimal protein, BigDecimal totalFat, BigDecimal carbs) {
}
