package br.com.gastrofactorapi.adapters.output.persistence.recipe.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gastrofactorapi.adapters.output.persistence.recipe.entity.RecipeEntity;

public interface RecipeRepository extends JpaRepository<RecipeEntity, UUID> {

}