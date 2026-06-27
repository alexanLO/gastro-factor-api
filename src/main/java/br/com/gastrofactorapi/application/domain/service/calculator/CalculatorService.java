package br.com.gastrofactorapi.application.domain.service.calculator;

import java.util.List;

import org.springframework.http.HttpStatus;

import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.domain.model.Calculator;
import br.com.gastrofactorapi.application.domain.model.FoodProfile;
import br.com.gastrofactorapi.application.domain.service.calculator.strategy.CalculatorStrategy;
import br.com.gastrofactorapi.infrastructure.exceptions.BusinessException;
import br.com.gastrofactorapi.infrastructure.exceptions.NotFoundException;
import br.com.gastrofactorapi.ports.input.CalculatorUseCase;
import br.com.gastrofactorapi.ports.output.FoodProfilePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CalculatorService implements CalculatorUseCase {

    private final FoodProfilePort foodProfilePort;
    private final List<CalculatorStrategy> caculatorStrategy;

    @Override
    public Calculator calculation(CalculatorCommand command) {
        log.info(" [CalculatorService] Iniciando chamada de calculo com os dados: {}", command);

        FoodProfile profileFood = foodProfilePort.getByName(command.foodName())
                .orElseThrow(() -> new NotFoundException(
                        "Alimento não encontrado para nome: " + command.foodName()));

        log.debug("Alimento encontrado com nome: {}", profileFood.foodName());

        Calculator result = caculatorStrategy.stream()
                .filter(s -> s.supports(command.typeWeight()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST.value(),
                        "Tipo de peso não suportado"))
                .calculate(command, profileFood);

        log.info("Cálculo finalizado com resultado: {}", result);
        return result;
    }

}
