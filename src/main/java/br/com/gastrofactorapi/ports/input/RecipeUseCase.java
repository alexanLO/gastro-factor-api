package br.com.gastrofactorapi.ports.input;

import java.util.List;
import java.util.UUID;

import br.com.gastrofactorapi.application.command.RecipeCommand;

public interface RecipeUseCase {

    UUID save(RecipeCommand fromRequestToCommand);

    List<RecipeCommand> listAll();


}
