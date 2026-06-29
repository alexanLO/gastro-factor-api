CREATE TABLE IF NOT EXISTS detailsentity (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    servings INTEGER,
    category VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS nutritionalentity (
    id UUID PRIMARY KEY,
    calories NUMERIC(38,2),
    protein NUMERIC(38,2),
    totalfat NUMERIC(38,2),
    carbs NUMERIC(38,2)
);

CREATE TABLE IF NOT EXISTS recipeentity (
    id UUID PRIMARY KEY,
    details_id UUID UNIQUE,
    nutritional_id UUID UNIQUE,
    CONSTRAINT fk_recipe_details FOREIGN KEY (details_id) REFERENCES detailsentity(id),
    CONSTRAINT fk_recipe_nutritional FOREIGN KEY (nutritional_id) REFERENCES nutritionalentity(id)
);

CREATE TABLE IF NOT EXISTS ingrediententity (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    netweight NUMERIC(38,2),
    correctionfactor NUMERIC(38,2),
    grossweight NUMERIC(38,2),
    cookingfactor NUMERIC(38,2),
    totalquantity NUMERIC(38,2),
    recipe_id UUID,
    CONSTRAINT fk_ingredient_recipe FOREIGN KEY (recipe_id) REFERENCES recipeentity(id)
);

CREATE TABLE IF NOT EXISTS preparationmethodentity (
    id UUID PRIMARY KEY,
    ordinationid INTEGER,
    title VARCHAR(255),
    description VARCHAR(255),
    recipe_id UUID,
    CONSTRAINT fk_preparationmethod_recipe FOREIGN KEY (recipe_id) REFERENCES recipeentity(id)
);

CREATE TABLE IF NOT EXISTS foodprofileentity (
    id UUID PRIMARY KEY,
    category VARCHAR(255) NOT NULL,
    food_name VARCHAR(255) NOT NULL,
    normalized_name VARCHAR(255) NOT NULL,
    alias VARCHAR(255),
    CONSTRAINT uk_food_catalog_normalized_name UNIQUE (normalized_name)
);

CREATE TABLE IF NOT EXISTS foodnutritionentity (
    id UUID PRIMARY KEY,
    food_id UUID NOT NULL,
    portion VARCHAR(255),
    calories NUMERIC(38,2),
    protein NUMERIC(38,2),
    carbohydrate NUMERIC(38,2),
    fat NUMERIC(38,2),
    CONSTRAINT fk_foodnutrition_foodprofile FOREIGN KEY (food_id) REFERENCES foodprofileentity(id)
);

CREATE TABLE IF NOT EXISTS foodpreparationentity (
    id UUID PRIMARY KEY,
    food_id UUID NOT NULL,
    preparation_type VARCHAR(255),
    correction_factor NUMERIC(38,2),
    coccion_factor NUMERIC(38,2),
    CONSTRAINT fk_foodpreparation_foodprofile FOREIGN KEY (food_id) REFERENCES foodprofileentity(id)
);

CREATE TABLE IF NOT EXISTS userentity (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    occupation VARCHAR(255),
    provider VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS refreshtokenentity (
    id UUID PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiresat TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL,
    user_id UUID,
    replaced_by_token VARCHAR(255),
    CONSTRAINT fk_refreshtoken_user FOREIGN KEY (user_id) REFERENCES userentity(id)
);

CREATE TABLE IF NOT EXISTS jwtblacklistentity (
    id UUID PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiresat TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_ingredient_recipe_id ON ingrediententity(recipe_id);
CREATE INDEX IF NOT EXISTS idx_preparationmethod_recipe_id ON preparationmethodentity(recipe_id);
CREATE INDEX IF NOT EXISTS idx_foodnutrition_food_id ON foodnutritionentity(food_id);
CREATE INDEX IF NOT EXISTS idx_foodpreparation_food_id ON foodpreparationentity(food_id);
CREATE INDEX IF NOT EXISTS idx_refreshtoken_user_id ON refreshtokenentity(user_id);