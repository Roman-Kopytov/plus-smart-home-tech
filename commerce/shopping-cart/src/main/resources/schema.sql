CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS shopping_cart
(
    shopping_cart_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username         varchar NOT NULL,
    status           varchar
);

CREATE TABLE IF NOT EXISTS shopping_cart_product
(
    id               UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_id       UUID,
    count            BIGINT,
    shopping_cart_id UUID,
    CONSTRAINT fk_product FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart (shopping_cart_id) ON DELETE CASCADE
);
