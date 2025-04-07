
ALTER TABLE movie
    ADD COLUMN user_id BIGINT,
    ADD CONSTRAINT fk_movie_user
        FOREIGN KEY (user_id) REFERENCES users(id);