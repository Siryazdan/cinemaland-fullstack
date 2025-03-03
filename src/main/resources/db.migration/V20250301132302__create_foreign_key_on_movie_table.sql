ALTER TABLE movie
    ADD CONSTRAINT fk_category
        FOREIGN KEY (movie_category_id)
            REFERENCES movie_category (id)
            On DELETE RESTRICT;