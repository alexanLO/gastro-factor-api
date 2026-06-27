package br.com.gastrofactorapi.adapters.output.persistence.calculator.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodNutritionEntity;

public interface FoodNutritionRepository extends JpaRepository<FoodNutritionEntity, UUID> {

}
