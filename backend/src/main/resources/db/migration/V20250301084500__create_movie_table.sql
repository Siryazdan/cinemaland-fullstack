CREATE TABLE IF NOT EXISTS movie
(
    id                VARCHAR(50) PRIMARY KEY,
    title             VARCHAR(255),
    director          VARCHAR(255),
    genre             VARCHAR(255),
    cast              VARCHAR(255),
    rating            VARCHAR(255),
    synopsis          TEXT,
    reviews_count     INT DEFAULT 0,
    movie_category_id VARCHAR(50)
);
