package br.com.gastrofactorapi.adapters.output.persistence.calculator.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodProfileEntity;

public interface FoodProfileRespository extends JpaRepository<FoodProfileEntity, UUID> {

    Optional<FoodProfileEntity> findByFoodNameIgnoreCase(String foodName);

    Optional<FoodProfileEntity> findByNormalizedName(String normalizedName);

}
