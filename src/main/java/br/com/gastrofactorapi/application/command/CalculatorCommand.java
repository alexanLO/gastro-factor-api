package br.com.gastrofactorapi.application.command;

import java.math.BigDecimal;

import br.com.gastrofactorapi.application.enums.TypeWeightEnum;

public record CalculatorCommand(String foodName, BigDecimal foodWeight, TypeWeightEnum typeWeight) {
}
