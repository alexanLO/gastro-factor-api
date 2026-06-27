package br.com.gastrofactorapi.ports.output;

import java.util.List;
import java.util.UUID;

import br.com.gastrofactorapi.application.command.RecipeCommand;

public interface RecipePort {

    UUID save(RecipeCommand command);

    List<RecipeCommand> listAll();

}
