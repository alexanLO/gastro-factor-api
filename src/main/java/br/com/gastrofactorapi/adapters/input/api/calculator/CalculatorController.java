package br.com.gastrofactorapi.adapters.input.api.calculator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gastrofactorapi.adapters.input.api.calculator.dto.request.CalculatorRequest;
import br.com.gastrofactorapi.adapters.input.api.calculator.dto.response.CalculatorResponse;
import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.domain.model.Calculator;
import br.com.gastrofactorapi.ports.input.CalculatorUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/calculator")
public class CalculatorController implements CalculatorSwagger {

    private final CalculatorApiMapper calculatorApiMapper;

    private final CalculatorUseCase calculatorUseCase;

    @Override
    @PostMapping
    public ResponseEntity<CalculatorResponse> calculator(@Valid @RequestBody CalculatorRequest request) {
        log.info("[CalculatorController] Iniciando chamada da calculadora com os dados: {}.", request);

        CalculatorCommand command = calculatorApiMapper.toCommandCalculator(request);

        Calculator result = calculatorUseCase.calculation(command);

        CalculatorResponse response = calculatorApiMapper.toResponseCalculator(result);

        log.info("[CalculatorController] Chamada da calculadora finalizada com sucesso.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
