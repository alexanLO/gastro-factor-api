package br.com.gastrofactorapi.application.command;

public record DetailsCommand(String name, String image, Integer servings, String category) {
}
