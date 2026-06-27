package br.com.gastrofactorapi.application.domain.service.calculator.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.domain.model.Calculator;
import br.com.gastrofactorapi.application.domain.model.FoodPreparation;
import br.com.gastrofactorapi.application.domain.model.FoodProfile;
import br.com.gastrofactorapi.application.enums.TypeWeightEnum;
import br.com.gastrofactorapi.shared.utils.EnumUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrossCalculatorStrategy implements CalculatorStrategy {

    @Override
    public boolean supports(TypeWeightEnum typeWeight) {
        EnumUtils.validateNotNull(typeWeight, "Tipo de peso não pode ser nulo");
        return typeWeight == TypeWeightEnum.GROSS;
    }

    @Override
    public Calculator calculate(CalculatorCommand command, FoodProfile foodProfile) {
        log.debug("[GrossCalculatorStrategy] Iniciando cálculo para peso bruto: {}", command.foodWeight());

        FoodPreparation foodPreparation = foodProfile.preparations().stream().findFirst().get();

        BigDecimal gross = command.foodWeight();
        BigDecimal net = gross.divide(foodPreparation.correctionFactor(), 3, RoundingMode.HALF_UP);
        BigDecimal cooked = net.divide(foodPreparation.coccionFactor(), 3, RoundingMode.HALF_UP);

        log.debug("Resultado do calculo: peso_bruto={}, peso_líquido={}, peso_cozido={}", gross, net, cooked);

        return new Calculator(command.foodName(), gross, net, cooked);
    }
}