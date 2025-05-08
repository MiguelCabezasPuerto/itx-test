DROP TABLE IF EXISTS PRICES;

CREATE TABLE PRICES (
    id_register INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    brand_id INT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    fare_id INT,
    priority_mod INT,
    price DECIMAL(10, 2),
    currency VARCHAR(3)
);

CREATE INDEX idx_brand ON PRICES (brand_id);
CREATE INDEX idx_start_date ON PRICES (start_date);
CREATE INDEX idx_end_date ON PRICES (end_date);
CREATE INDEX idx_product_brand_date ON PRICES (product_id, brand_id, start_date, end_date);


