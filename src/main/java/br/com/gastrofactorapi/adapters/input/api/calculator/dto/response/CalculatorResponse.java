package br.com.gastrofactorapi.adapters.input.api.calculator.dto.response;

import java.math.BigDecimal;

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
public class CalculatorResponse {
    private String foodName;
    private BigDecimal grossWeight;
    private BigDecimal netWeight;
    private BigDecimal cookedWeight;
}
