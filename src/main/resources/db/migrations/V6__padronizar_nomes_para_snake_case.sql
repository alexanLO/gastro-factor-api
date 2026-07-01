-- V6__padronizar_nomes_para_snake_case.sql
-- Padroniza nomes de tabelas, colunas, constraints e indices para snake_case.

DO $$
BEGIN
	-- Tabelas
	IF to_regclass('detailsentity') IS NOT NULL AND to_regclass('details_entity') IS NULL THEN
		ALTER TABLE detailsentity RENAME TO details_entity;
	END IF;

	IF to_regclass('userentity') IS NOT NULL AND to_regclass('user_entity') IS NULL THEN
		ALTER TABLE userentity RENAME TO user_entity;
	END IF;

	-- Colunas
	IF to_regclass('nutritional_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1
		   FROM information_schema.columns
		   WHERE table_schema = 'public'
			 AND table_name = 'nutritional_entity'
			 AND column_name = 'totalfat'
	   )
	   AND NOT EXISTS (
		   SELECT 1
		   FROM information_schema.columns
		   WHERE table_schema = 'public'
			 AND table_name = 'nutritional_entity'
			 AND column_name = 'total_fat'
	   ) THEN
		ALTER TABLE nutritional_entity RENAME COLUMN totalfat TO total_fat;
	END IF;

	IF to_regclass('ingredient_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'netweight'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'net_weight'
	   ) THEN
		ALTER TABLE ingredient_entity RENAME COLUMN netweight TO net_weight;
	END IF;

	IF to_regclass('ingredient_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'correctionfactor'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'correction_factor'
	   ) THEN
		ALTER TABLE ingredient_entity RENAME COLUMN correctionfactor TO correction_factor;
	END IF;

	IF to_regclass('ingredient_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'grossweight'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'gross_weight'
	   ) THEN
		ALTER TABLE ingredient_entity RENAME COLUMN grossweight TO gross_weight;
	END IF;

	IF to_regclass('ingredient_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'cookingfactor'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'cooking_factor'
	   ) THEN
		ALTER TABLE ingredient_entity RENAME COLUMN cookingfactor TO cooking_factor;
	END IF;

	IF to_regclass('ingredient_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'totalquantity'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'ingredient_entity' AND column_name = 'total_quantity'
	   ) THEN
		ALTER TABLE ingredient_entity RENAME COLUMN totalquantity TO total_quantity;
	END IF;

	IF to_regclass('preparation_method_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'preparation_method_entity' AND column_name = 'ordinationid'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'preparation_method_entity' AND column_name = 'ordination_id'
	   ) THEN
		ALTER TABLE preparation_method_entity RENAME COLUMN ordinationid TO ordination_id;
	END IF;

	IF to_regclass('refresh_token_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'refresh_token_entity' AND column_name = 'expiresat'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'refresh_token_entity' AND column_name = 'expires_at'
	   ) THEN
		ALTER TABLE refresh_token_entity RENAME COLUMN expiresat TO expires_at;
	END IF;

	IF to_regclass('jwt_blacklist_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'jwt_blacklist_entity' AND column_name = 'expiresat'
	   )
	   AND NOT EXISTS (
		   SELECT 1 FROM information_schema.columns
		   WHERE table_schema = 'public' AND table_name = 'jwt_blacklist_entity' AND column_name = 'expires_at'
	   ) THEN
		ALTER TABLE jwt_blacklist_entity RENAME COLUMN expiresat TO expires_at;
	END IF;

	-- Constraints
	IF to_regclass('recipe_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'fk_recipe_details'
			 AND conrelid = 'recipe_entity'::regclass
	   ) THEN
		ALTER TABLE recipe_entity RENAME CONSTRAINT fk_recipe_details TO fk_recipe_entity_details_entity;
	END IF;

	IF to_regclass('recipe_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'fk_recipe_nutritional'
			 AND conrelid = 'recipe_entity'::regclass
	   ) THEN
		ALTER TABLE recipe_entity RENAME CONSTRAINT fk_recipe_nutritional TO fk_recipe_entity_nutritional_entity;
	END IF;

	IF to_regclass('ingredient_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'fk_ingredient_recipe'
			 AND conrelid = 'ingredient_entity'::regclass
	   ) THEN
		ALTER TABLE ingredient_entity RENAME CONSTRAINT fk_ingredient_recipe TO fk_ingredient_entity_recipe_entity;
	END IF;

	IF to_regclass('preparation_method_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'fk_preparationmethod_recipe'
			 AND conrelid = 'preparation_method_entity'::regclass
	   ) THEN
		ALTER TABLE preparation_method_entity RENAME CONSTRAINT fk_preparationmethod_recipe TO fk_preparation_method_entity_recipe_entity;
	END IF;

	IF to_regclass('food_nutrition_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'fk_foodnutrition_foodprofile'
			 AND conrelid = 'food_nutrition_entity'::regclass
	   ) THEN
		ALTER TABLE food_nutrition_entity RENAME CONSTRAINT fk_foodnutrition_foodprofile TO fk_food_nutrition_entity_food_profile_entity;
	END IF;

	IF to_regclass('food_preparation_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'fk_foodpreparation_foodprofile'
			 AND conrelid = 'food_preparation_entity'::regclass
	   ) THEN
		ALTER TABLE food_preparation_entity RENAME CONSTRAINT fk_foodpreparation_foodprofile TO fk_food_preparation_entity_food_profile_entity;
	END IF;

	IF to_regclass('refresh_token_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'fk_refreshtoken_user'
			 AND conrelid = 'refresh_token_entity'::regclass
	   ) THEN
		ALTER TABLE refresh_token_entity RENAME CONSTRAINT fk_refreshtoken_user TO fk_refresh_token_entity_user_entity;
	END IF;

	IF to_regclass('food_profile_entity') IS NOT NULL
	   AND EXISTS (
		   SELECT 1 FROM pg_constraint
		   WHERE conname = 'uk_food_catalog_normalized_name'
			 AND conrelid = 'food_profile_entity'::regclass
	   ) THEN
		ALTER TABLE food_profile_entity RENAME CONSTRAINT uk_food_catalog_normalized_name TO uk_food_profile_entity_normalized_name;
	END IF;

	-- Indices
	IF to_regclass('idx_ingredient_recipe_id') IS NOT NULL
	   AND to_regclass('idx_ingredient_entity_recipe_id') IS NULL THEN
		ALTER INDEX idx_ingredient_recipe_id RENAME TO idx_ingredient_entity_recipe_id;
	END IF;

	IF to_regclass('idx_preparationmethod_recipe_id') IS NOT NULL
	   AND to_regclass('idx_preparation_method_entity_recipe_id') IS NULL THEN
		ALTER INDEX idx_preparationmethod_recipe_id RENAME TO idx_preparation_method_entity_recipe_id;
	END IF;

	IF to_regclass('idx_foodnutrition_food_id') IS NOT NULL
	   AND to_regclass('idx_food_nutrition_entity_food_id') IS NULL THEN
		ALTER INDEX idx_foodnutrition_food_id RENAME TO idx_food_nutrition_entity_food_id;
	END IF;

	IF to_regclass('idx_foodpreparation_food_id') IS NOT NULL
	   AND to_regclass('idx_food_preparation_entity_food_id') IS NULL THEN
		ALTER INDEX idx_foodpreparation_food_id RENAME TO idx_food_preparation_entity_food_id;
	END IF;

	IF to_regclass('idx_refreshtoken_user_id') IS NOT NULL
	   AND to_regclass('idx_refresh_token_entity_user_id') IS NULL THEN
		ALTER INDEX idx_refreshtoken_user_id RENAME TO idx_refresh_token_entity_user_id;
	END IF;

	IF to_regclass('uk_userentity_email') IS NOT NULL
	   AND to_regclass('uk_user_entity_email') IS NULL THEN
		ALTER INDEX uk_userentity_email RENAME TO uk_user_entity_email;
	END IF;
END $$;
