package br.com.gastrofactorapi.application.domain.service.calculator.strategy;

import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.domain.model.Calculator;
import br.com.gastrofactorapi.application.domain.model.FoodProfile;
import br.com.gastrofactorapi.application.enums.TypeWeightEnum;

public interface CalculatorStrategy {

    boolean supports(TypeWeightEnum typeWeight);

    Calculator calculate(CalculatorCommand command, FoodProfile foodProfile);
}
