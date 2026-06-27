package br.com.gastrofactorapi.infrastructure.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.gastrofactorapi.application.domain.service.RecipeService;
import br.com.gastrofactorapi.application.domain.service.calculator.CalculatorService;
import br.com.gastrofactorapi.application.domain.service.calculator.strategy.CalculatorStrategy;
import br.com.gastrofactorapi.application.domain.service.calculator.strategy.CookedCalculatorStrategy;
import br.com.gastrofactorapi.application.domain.service.calculator.strategy.GrossCalculatorStrategy;
import br.com.gastrofactorapi.application.domain.service.calculator.strategy.NetCalculatorStrategy;
import br.com.gastrofactorapi.ports.input.CalculatorUseCase;
import br.com.gastrofactorapi.ports.input.RecipeUseCase;
import br.com.gastrofactorapi.ports.output.FoodProfilePort;
import br.com.gastrofactorapi.ports.output.RecipePort;

@Configuration
public class BeanConfig {

    @Bean
    public RecipeUseCase recipeUseCase(RecipePort recipePort) {
        return new RecipeService(recipePort);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CalculatorUseCase calculatorUseCase(FoodProfilePort foodProfilePort, List<CalculatorStrategy> calculatorStrategy){
        return new CalculatorService(foodProfilePort, calculatorStrategy);
    }

    @Bean
    public GrossCalculatorStrategy grossCalculatorStrategy() {
        return new GrossCalculatorStrategy();
    }

    @Bean
    public NetCalculatorStrategy netCalculatorStrategy() {
        return new NetCalculatorStrategy();
    }

    @Bean
    public CookedCalculatorStrategy cookedCalculatorStrategy() {
        return new CookedCalculatorStrategy();
    }
}
