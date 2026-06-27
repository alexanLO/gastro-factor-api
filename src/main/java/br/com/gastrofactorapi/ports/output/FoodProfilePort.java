package br.com.gastrofactorapi.ports.output;

import java.util.Optional;

import br.com.gastrofactorapi.application.domain.model.FoodProfile;

public interface FoodProfilePort {

    Optional<FoodProfile> getByName(String foodName);

}
