CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS products
(
    product_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_name     varchar NOT NULL,
    description      varchar NOT NULL,
    image_src        varchar,
    quantity_state   varchar,
    product_state    varchar,
    rating           DOUBLE PRECISION,
    product_category varchar,
    price            DOUBLE PRECISION
);