CREATE TABLE movie (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        Director VARCHAR(300),
                        genre VARCHAR(255) NOT NULL,
                        Cast VARCHAR(1000),
                        rating DECIMAL(3, 1) NOT NULL,
                        Synopsis VARCHAR(1000)



);
