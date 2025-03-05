CREATE TABLE movie_actor(
    movie_id VARCHAR(50) NOT NULL,
    actor_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES actor(id) ON DELETE CASCADE
);