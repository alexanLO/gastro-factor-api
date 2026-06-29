package br.com.gastrofactorapi.application.domain.service.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.exceptions.ApplicationBusinessException;
import br.com.gastrofactorapi.application.exceptions.ApplicationNotFoundException;
import br.com.gastrofactorapi.application.domain.model.Calculator;
import br.com.gastrofactorapi.application.domain.model.FoodProfile;
import br.com.gastrofactorapi.application.domain.service.calculator.strategy.CalculatorStrategy;
import br.com.gastrofactorapi.application.enums.TypeWeightEnum;
import br.com.gastrofactorapi.ports.output.FoodProfilePort;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @Mock
    private FoodProfilePort foodProfilePort;

    @Mock
    private CalculatorStrategy calculatorStrategy;

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService(foodProfilePort, List.of(calculatorStrategy));
    }

    @Test
    void shouldThrowNotFoundWhenFoodDoesNotExist() {
        CalculatorCommand command = new CalculatorCommand("banana", new BigDecimal("100"), TypeWeightEnum.NET);
        when(foodProfilePort.getByName("banana")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> calculatorService.calculation(command))
            .isInstanceOf(ApplicationNotFoundException.class)
                .hasMessageContaining("Alimento não encontrado");
    }

    @Test
    void shouldCalculateUsingSupportedStrategy() {
        CalculatorCommand command = new CalculatorCommand("banana", new BigDecimal("100"), TypeWeightEnum.NET);
        FoodProfile profile = new FoodProfile("fruta", "banana", "banana", "", List.of(), List.of());
        Calculator expected = new Calculator("banana", new BigDecimal("120"), new BigDecimal("100"), new BigDecimal("90"));

        when(foodProfilePort.getByName("banana")).thenReturn(Optional.of(profile));
        when(calculatorStrategy.supports(TypeWeightEnum.NET)).thenReturn(true);
        when(calculatorStrategy.calculate(command, profile)).thenReturn(expected);

        Calculator result = calculatorService.calculation(command);

        assertThat(result).isEqualTo(expected);
        verify(calculatorStrategy).calculate(command, profile);
    }

    @Test
    void shouldThrowBusinessExceptionWhenNoStrategySupportsType() {
        CalculatorCommand command = new CalculatorCommand("banana", new BigDecimal("100"), TypeWeightEnum.COOKED);
        FoodProfile profile = new FoodProfile("fruta", "banana", "banana", "", List.of(), List.of());

        when(foodProfilePort.getByName("banana")).thenReturn(Optional.of(profile));
        when(calculatorStrategy.supports(TypeWeightEnum.COOKED)).thenReturn(false);

        assertThatThrownBy(() -> calculatorService.calculation(command))
            .isInstanceOf(ApplicationBusinessException.class)
            .satisfies(ex -> assertThat(((ApplicationBusinessException) ex).getStatusCode()).isEqualTo(400));
    }
}