CREATE TABLE movie
(
    id       VARCHAR(50) PRIMARY KEY,
    title    VARCHAR(255)  NOT NULL,
    director VARCHAR(255),
    genre    VARCHAR(255)  NOT NULL,
    cast     VARCHAR(255),
    rating   DECIMAL(3, 1) NOT NULL,
    synopsis TEXT,
    release_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
