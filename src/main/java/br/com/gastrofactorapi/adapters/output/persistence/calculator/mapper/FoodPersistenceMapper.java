package br.com.gastrofactorapi.adapters.output.persistence.calculator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodNutritionEntity;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodPreparationEntity;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodProfileEntity;
import br.com.gastrofactorapi.application.domain.model.FoodNutrition;
import br.com.gastrofactorapi.application.domain.model.FoodPreparation;
import br.com.gastrofactorapi.application.domain.model.FoodProfile;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FoodPersistenceMapper {

    FoodProfile toModelFoodProfile(FoodProfileEntity entity);

    FoodNutrition toModelFoodNutrition(FoodNutritionEntity entity);

    FoodPreparation toModelFoodPreparation(FoodPreparationEntity entity);
}
