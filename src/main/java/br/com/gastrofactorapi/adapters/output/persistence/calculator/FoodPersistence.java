package br.com.gastrofactorapi.adapters.output.persistence.calculator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodProfileEntity;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.mapper.FoodPersistenceMapper;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.repository.FoodProfileRespository;
import br.com.gastrofactorapi.application.domain.model.FoodProfile;
import br.com.gastrofactorapi.ports.output.FoodProfilePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FoodPersistence implements FoodProfilePort {

    private final FoodPersistenceMapper foodPersistenceMapper;
    private final FoodProfileRespository foodProfileRespository;

    @Override
    public Optional<FoodProfile> getByName(String foodName) {
        log.debug("Consultando alimento pelo nome: {}", foodName);

        Optional<FoodProfileEntity> foodFound = foodProfileRespository
                .findByFoodNameIgnoreCase(foodName);

        Optional<FoodProfile> result = foodFound.map(food -> {
            log.debug(
                    "Alimento encontrado: {}", food);

            return foodPersistenceMapper.toModelFoodProfile(food);
        });

        if (result.isEmpty()) {
            log.warn("Alimento não encontrado para nome: {}", foodName);
        }

        return result;
    }

}
