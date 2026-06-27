package br.com.gastrofactorapi.application.command;

public record PreparationMethodCommand(Integer ordinationId, String title, String description) {
}
