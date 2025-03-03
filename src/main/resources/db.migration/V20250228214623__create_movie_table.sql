CREATE TABLE movie (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       director VARCHAR(255),
                       genre VARCHAR(255) NOT NULL,
                       cast VARCHAR(255),
                       rating DECIMAL(3, 1) NOT NULL,
                       synopsis TEXT

);
