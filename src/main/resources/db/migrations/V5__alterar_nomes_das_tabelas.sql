-- V5__rename_tables_to_snake_case.sql

ALTER TABLE foodnutritionentity RENAME TO food_nutrition_entity;
ALTER TABLE foodpreparationentity RENAME TO food_preparation_entity;
ALTER TABLE foodprofileentity RENAME TO food_profile_entity;
ALTER TABLE ingrediententity RENAME TO ingredient_entity;
ALTER TABLE jwtblacklistentity RENAME TO jwt_blacklist_entity;
ALTER TABLE nutritionalentity RENAME TO nutritional_entity;
ALTER TABLE preparationmethodentity RENAME TO preparation_method_entity;
ALTER TABLE recipeentity RENAME TO recipe_entity;
ALTER TABLE refreshtokenentity RENAME TO refresh_token_entity;
