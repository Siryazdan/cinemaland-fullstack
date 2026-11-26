-- Users who can log in. Passwords are stored only as BCrypt hashes.
CREATE TABLE app_user
(
    id            VARCHAR(50)  PRIMARY KEY NOT NULL,
    email         VARCHAR(255)             NOT NULL,
    password_hash VARCHAR(100)             NOT NULL,
    display_name  VARCHAR(100)             NOT NULL,
    created_at    DATETIME                 NOT NULL,
    CONSTRAINT app_user_email_uk UNIQUE (email)
);

-- Every movie, category and actor belongs to exactly one user (their own collection).
ALTER TABLE movie ADD COLUMN user_id VARCHAR(50) NULL;
ALTER TABLE movie_category ADD COLUMN user_id VARCHAR(50) NULL;
ALTER TABLE actor ADD COLUMN user_id VARCHAR(50) NULL;

ALTER TABLE movie
    ADD CONSTRAINT movie_user_fk FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE;
ALTER TABLE movie_category
    ADD CONSTRAINT movie_category_user_fk FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE;
ALTER TABLE actor
    ADD CONSTRAINT actor_user_fk FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE;
