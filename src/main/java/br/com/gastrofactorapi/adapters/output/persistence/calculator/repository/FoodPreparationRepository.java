package br.com.gastrofactorapi.adapters.output.persistence.calculator.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodPreparationEntity;

public interface FoodPreparationRepository extends JpaRepository<FoodPreparationEntity, UUID> {

}
