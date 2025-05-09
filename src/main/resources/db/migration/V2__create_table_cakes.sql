CREATE TABLE IF NOT EXISTS cakes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    flavor VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    content TEXT NOT NULL,
    nutritional_table JSON NOT NULL
);

CREATE TABLE IF NOT EXISTS sizes_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    cake_id BIGINT NOT NULL,
    FOREIGN KEY (cake_id) REFERENCES cakes(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cakes_categories (
    cake_id SERIAL,
    category_id BIGINT,
    PRIMARY KEY (cake_id, category_id),
    FOREIGN KEY (cake_id) REFERENCES cakes(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);