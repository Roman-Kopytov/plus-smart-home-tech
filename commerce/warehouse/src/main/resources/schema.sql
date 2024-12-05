DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS warehouse_product
(
    product_id UUID PRIMARY KEY,
    fragile    boolean          NOT NULL,
    width      double precision NOT NULL,
    height     double precision NOT NULL,
    depth      double precision NOT NULL,
    weight     double precision NOT NULL,
    quantity   bigint
);

CREATE TABLE IF NOT EXISTS booking
(
    booking_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL,
    delivery_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS booking_product
(
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_id UUID,
    count      BIGINT,
    booking_id UUID NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "order"
(
    order_id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    shopping_cart_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS "order_product"
(
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_id UUID,
    count      BIGINT,
    order_id   UUID NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (order_id) REFERENCES "order" (order_id) ON DELETE CASCADE
);


