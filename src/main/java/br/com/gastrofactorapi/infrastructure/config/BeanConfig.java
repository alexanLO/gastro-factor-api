package br.com.gastrofactorapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.gastrofactorapi.application.domain.service.RecipeService;
import br.com.gastrofactorapi.ports.input.RecipeUseCase;
import br.com.gastrofactorapi.ports.output.RecipePort;

@Configuration
public class BeanConfig {

    @Bean
    public RecipeUseCase recipeUseCase(RecipePort recipePort) {
        return new RecipeService(recipePort);
    }
}
