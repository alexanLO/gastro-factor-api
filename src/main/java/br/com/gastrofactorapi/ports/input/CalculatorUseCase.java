package br.com.gastrofactorapi.ports.input;

import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.domain.model.Calculator;

public interface CalculatorUseCase {

    Calculator calculation(CalculatorCommand command);
    
}
