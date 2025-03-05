CREATE TABLE movie
(
    id                VARCHAR(50) PRIMARY KEY,
    title             VARCHAR(255)  NOT NULL,
    director          VARCHAR(255),
    genre             VARCHAR(255)  NOT NULL,
    cast              VARCHAR(255),
    rating            DECIMAL(3, 1) NOT NULL,
    movie_category_id VARCHAR(50),
    synopsis          TEXT,
    reviews_count     INT
);
