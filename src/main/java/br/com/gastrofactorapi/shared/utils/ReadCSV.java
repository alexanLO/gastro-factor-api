package br.com.gastrofactorapi.shared.utils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodNutritionEntity;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodPreparationEntity;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.entity.FoodProfileEntity;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.repository.FoodNutritionRepository;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.repository.FoodPreparationRepository;
import br.com.gastrofactorapi.adapters.output.persistence.calculator.repository.FoodProfileRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.seed.enabled", havingValue = "true")
public class ReadCSV implements CommandLineRunner {

	private final FoodProfileRespository foodProfileRepository;

	private final FoodNutritionRepository nutritionRepository;

	private final FoodPreparationRepository preparationRepository;

	@Override
	public void run(String... args) throws Exception {

		boolean hasData = foodProfileRepository.count() > 0;

		if (hasData) {
			log.info("Tabela food_catalog ja possui dados. Seed ignorado.");
			return;
		}

		ClassPathResource resource = new ClassPathResource(
				"db/csv/ListFoods.csv");

		List<FoodNutritionEntity> nutritions = new ArrayList<>();

		List<FoodPreparationEntity> preparations = new ArrayList<>();

		try (
				Reader reader = new InputStreamReader(
						resource.getInputStream())) {

			CSVReader csvReader = new CSVReaderBuilder(reader)
					.withSkipLines(1)
					.build();

			String[] line;

			while ((line = csvReader.readNext()) != null) {

				String category = line[1];

				String foodName = line[2];

				String normalizedName = normalize(foodName);

				Optional<FoodProfileEntity> existingFood = foodProfileRepository.findByNormalizedName(
						normalizedName);

				FoodProfileEntity catalog;

				if (existingFood.isPresent()) {

					catalog = existingFood.get();

				} else {

					catalog = new FoodProfileEntity();

					catalog.setCategory(category);

					catalog.setFoodName(foodName);

					catalog.setNormalizedName(
							normalizedName);

					catalog = foodProfileRepository.save(catalog);
				}

				FoodNutritionEntity nutrition = new FoodNutritionEntity();

				nutrition.setFood(catalog);

				nutrition.setPortion(line[6]);

				nutrition.setCalories(decimal(line[7]));

				nutrition.setProtein(decimal(line[8]));

				nutrition.setCarbohydrate(decimal(line[9]));

				nutrition.setFat(decimal(line[10]));

				nutritions.add(nutrition);

				FoodPreparationEntity preparation = new FoodPreparationEntity();

				preparation.setFood(catalog);

				preparation.setPreparationType(line[5]);

				preparation.setCorrectionFactor(
						decimal(line[3]));

				preparation.setCoccionFactor(
						decimal(line[4]));

				preparations.add(preparation);
			}

			nutritionRepository.saveAll(nutritions);

			preparationRepository.saveAll(preparations);

			log.info("CSV importado com sucesso.");
			log.info("Nutritions: {}", nutritions.size());
			log.info("Preparations: {}", preparations.size());
		}
	}

	private BigDecimal decimal(String value) {

		if (value == null || value.isBlank()) {
			return null;
		}

		return new BigDecimal(value);
	}

	private String normalize(String value) {

		return Normalizer
				.normalize(
						value,
						Normalizer.Form.NFD)
				.replaceAll(
						"\\p{InCombiningDiacriticalMarks}+",
						"")
				.toLowerCase()
				.trim();
	}
}