CREATE TABLE file_data
(
    id             varchar(50) PRIMARY KEY,
    file_path      varchar(255) NOT NULL,
    file_name      varchar(255) NOT NULL,
    file_extension varchar(10)  NOT NULL,
    mimetype       varchar(100) NOT NULL,
    file_size      BIGINT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rel_id         varchar(50)  NOT NULL
);