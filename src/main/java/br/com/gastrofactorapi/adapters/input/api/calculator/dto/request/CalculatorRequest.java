package br.com.gastrofactorapi.adapters.input.api.calculator.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorRequest {
    @NotBlank(message = "Nome do alimento é obrigatório")
    private String foodName;
    
    @NotNull(message = "Peso do alimento é obrigatório")
    @Positive(message = "Peso deve ser um valor positivo")
    private BigDecimal foodWeight;
    
    @NotBlank(message = "Tipo de peso é obrigatório")
    private String typeWeight;
}