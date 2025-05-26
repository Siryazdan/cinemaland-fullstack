ALTER TABLE movie
    ADD CONSTRAINT movie_movie_category_id_fk
        FOREIGN KEY (movie_category_id)
            REFERENCES movie_category (id)
            ON DELETE RESTRICT;