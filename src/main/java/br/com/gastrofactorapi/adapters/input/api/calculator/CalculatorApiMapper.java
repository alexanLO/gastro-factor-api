package br.com.gastrofactorapi.adapters.input.api.calculator;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import br.com.gastrofactorapi.adapters.input.api.calculator.dto.request.CalculatorRequest;
import br.com.gastrofactorapi.adapters.input.api.calculator.dto.response.CalculatorResponse;
import br.com.gastrofactorapi.application.command.CalculatorCommand;
import br.com.gastrofactorapi.application.domain.model.Calculator;
import br.com.gastrofactorapi.application.enums.TypeWeightEnum;
import br.com.gastrofactorapi.shared.utils.EnumUtils;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalculatorApiMapper {

    CalculatorCommand toCommandCalculator(CalculatorRequest request);

    CalculatorResponse toResponseCalculator(Calculator result);

    default TypeWeightEnum mapTypeWeight(String typeWeight) {
        return EnumUtils.getEnumFromStringOrThrow(TypeWeightEnum.class, typeWeight);
    }

}