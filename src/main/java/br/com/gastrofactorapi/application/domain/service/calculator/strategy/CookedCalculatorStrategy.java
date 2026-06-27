package br.com.gastrofactorapi.application.domain.service.calculator.strategy;

import java.math.BigDecimal;

import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.domain.model.Calculator;
import br.com.gastrofactorapi.application.domain.model.FoodPreparation;
import br.com.gastrofactorapi.application.domain.model.FoodProfile;
import br.com.gastrofactorapi.application.enums.TypeWeightEnum;
import br.com.gastrofactorapi.shared.utils.EnumUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookedCalculatorStrategy implements CalculatorStrategy {

    @Override
    public boolean supports(TypeWeightEnum typeWeight) {
        EnumUtils.validateNotNull(typeWeight, "Tipo de peso não pode ser nulo");
        return typeWeight == TypeWeightEnum.COOKED;
    }

    @Override
    public Calculator calculate(CalculatorCommand command, FoodProfile foodProfile) {
        log.debug("[CookedCalculatorStrategy] Iniciando cálculo para peso cozido: {}", command.foodWeight());

        FoodPreparation preparation = foodProfile.preparations().stream().findFirst().get();

        BigDecimal cooked = command.foodWeight();
        BigDecimal net = cooked.multiply(preparation.coccionFactor());
        BigDecimal gross = net.multiply(preparation.correctionFactor());

        log.debug("Resultado do calculo: peso_bruto={}, peso_líquido={}, peso_cozido={}", gross, net, cooked);

        return new Calculator(command.foodName(), gross, net, cooked);
    }
}