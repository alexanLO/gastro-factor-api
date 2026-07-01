-- V7__inserir_3_alimentos_cozidos.sql
-- Insere 3 alimentos (perfil, nutricao e preparo) de forma idempotente.

BEGIN;

-- 1) Peito de Frango (Aves)
WITH upsert_profile AS (
    INSERT INTO food_profile_entity (id, category, food_name, normalized_name, alias)
    VALUES (
        '11111111-1111-1111-1111-111111111001',
        'Aves',
        'Peito de Frango',
        'peito de frango',
        NULL
    )
    ON CONFLICT (normalized_name) DO UPDATE
    SET category = EXCLUDED.category,
        food_name = EXCLUDED.food_name
    RETURNING id
), profile AS (
    SELECT id FROM upsert_profile
    UNION
    SELECT id FROM food_profile_entity WHERE normalized_name = 'peito de frango'
)
INSERT INTO food_nutrition_entity (id, food_id, portion, calories, protein, carbohydrate, fat)
SELECT
    '22222222-2222-2222-2222-222222222001',
    p.id,
    '1 unidade',
    207,
    14.3,
    8.4,
    3.8
FROM profile p
WHERE NOT EXISTS (
    SELECT 1
    FROM food_nutrition_entity fn
    WHERE fn.food_id = p.id
      AND fn.portion = '1 unidade'
      AND fn.calories = 207
      AND fn.protein = 14.3
      AND fn.carbohydrate = 8.4
      AND fn.fat = 3.8
);

WITH profile AS (
    SELECT id FROM food_profile_entity WHERE normalized_name = 'peito de frango'
)
INSERT INTO food_preparation_entity (id, food_id, preparation_type, correction_factor, coccion_factor)
SELECT
    '33333333-3333-3333-3333-333333333001',
    p.id,
    'Cozido',
    1.32,
    1.52
FROM profile p
WHERE NOT EXISTS (
    SELECT 1
    FROM food_preparation_entity fp
    WHERE fp.food_id = p.id
      AND fp.preparation_type = 'Cozido'
      AND fp.correction_factor = 1.32
      AND fp.coccion_factor = 1.52
);

-- 2) Peru (Aves)
WITH upsert_profile AS (
    INSERT INTO food_profile_entity (id, category, food_name, normalized_name, alias)
    VALUES (
        '11111111-1111-1111-1111-111111111025',
        'Aves',
        'Peru',
        'peru',
        NULL
    )
    ON CONFLICT (normalized_name) DO UPDATE
    SET category = EXCLUDED.category,
        food_name = EXCLUDED.food_name
    RETURNING id
), profile AS (
    SELECT id FROM upsert_profile
    UNION
    SELECT id FROM food_profile_entity WHERE normalized_name = 'peru'
)
INSERT INTO food_nutrition_entity (id, food_id, portion, calories, protein, carbohydrate, fat)
SELECT
    '22222222-2222-2222-2222-222222222025',
    p.id,
    '100g',
    369,
    15.5,
    12.9,
    27.6
FROM profile p
WHERE NOT EXISTS (
    SELECT 1
    FROM food_nutrition_entity fn
    WHERE fn.food_id = p.id
      AND fn.portion = '100g'
      AND fn.calories = 369
      AND fn.protein = 15.5
      AND fn.carbohydrate = 12.9
      AND fn.fat = 27.6
);

WITH profile AS (
    SELECT id FROM food_profile_entity WHERE normalized_name = 'peru'
)
INSERT INTO food_preparation_entity (id, food_id, preparation_type, correction_factor, coccion_factor)
SELECT
    '33333333-3333-3333-3333-333333333025',
    p.id,
    'Cozido',
    1.08,
    1.57
FROM profile p
WHERE NOT EXISTS (
    SELECT 1
    FROM food_preparation_entity fp
    WHERE fp.food_id = p.id
      AND fp.preparation_type = 'Cozido'
      AND fp.correction_factor = 1.08
      AND fp.coccion_factor = 1.57
);

-- 3) Picanha (Bovinos)
WITH upsert_profile AS (
    INSERT INTO food_profile_entity (id, category, food_name, normalized_name, alias)
    VALUES (
        '11111111-1111-1111-1111-111111111049',
        'Bovinos',
        'Picanha',
        'picanha',
        NULL
    )
    ON CONFLICT (normalized_name) DO UPDATE
    SET category = EXCLUDED.category,
        food_name = EXCLUDED.food_name
    RETURNING id
), profile AS (
    SELECT id FROM upsert_profile
    UNION
    SELECT id FROM food_profile_entity WHERE normalized_name = 'picanha'
)
INSERT INTO food_nutrition_entity (id, food_id, portion, calories, protein, carbohydrate, fat)
SELECT
    '22222222-2222-2222-2222-222222222049',
    p.id,
    '1 porção',
    144,
    39.5,
    20.5,
    29.5
FROM profile p
WHERE NOT EXISTS (
    SELECT 1
    FROM food_nutrition_entity fn
    WHERE fn.food_id = p.id
      AND fn.portion = '1 porção'
      AND fn.calories = 144
      AND fn.protein = 39.5
      AND fn.carbohydrate = 20.5
      AND fn.fat = 29.5
);

WITH profile AS (
    SELECT id FROM food_profile_entity WHERE normalized_name = 'picanha'
)
INSERT INTO food_preparation_entity (id, food_id, preparation_type, correction_factor, coccion_factor)
SELECT
    '33333333-3333-3333-3333-333333333049',
    p.id,
    'Cozido',
    1.08,
    2.39
FROM profile p
WHERE NOT EXISTS (
    SELECT 1
    FROM food_preparation_entity fp
    WHERE fp.food_id = p.id
      AND fp.preparation_type = 'Cozido'
      AND fp.correction_factor = 1.08
      AND fp.coccion_factor = 2.39
);

COMMIT;
