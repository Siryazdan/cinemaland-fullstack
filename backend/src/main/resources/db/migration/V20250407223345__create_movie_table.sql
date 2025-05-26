CREATE TABLE IF NOT EXISTS movie (
                       id VARCHAR(255) PRIMARY KEY,
                       title VARCHAR(255),
                       director VARCHAR(255),
                       genre VARCHAR(255),
                       cast VARCHAR(255),
                       rating VARCHAR(255),
                       synopsis TEXT,
                       reviews_count INT,
                       movie_category_id VARCHAR(255),
                       user_id VARCHAR(255),
                       FOREIGN KEY (user_id) REFERENCES user(id)
);