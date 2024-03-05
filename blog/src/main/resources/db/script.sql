DROP TABLE IF EXISTS commentaries;
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(100) NOT NULL UNIQUE,
    email             VARCHAR(100) NOT NULL UNIQUE,
    verification_сode varchar,
    verified          Boolean,
    banned            BOOLEAN,
    year_of_birth     INT CHECK (year_of_birth >= 1900 AND year_of_birth <= 2023),
    password          VARCHAR,
    role              VARCHAR
);


CREATE TABLE IF NOT EXISTS articles
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    content      TEXT         NOT NULL,
    user_id      INT,
    publish_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS commentaries
(
    id           SERIAL PRIMARY KEY,
    commentary   VARCHAR NOT NULL,
    user_id      INT,
    article_id   INT,
    publish_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (article_id) REFERENCES articles (id)
);
UPDATE users
SET role = 'ROLE_ADMIN'
WHERE name = 'admin'
;
