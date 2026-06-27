package br.com.gastrofactorapi.application.domain.model;

import java.util.List;

public record FoodProfile(String category, String foodName, String normalizedName, String alias,
        List<FoodPreparation> preparations, List<FoodNutrition> nutritions) {
}
