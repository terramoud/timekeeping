-- -----------------------------------------------------
-- Schema timekeeping
-- -----------------------------------------------------
DROP DATABASE IF EXISTS timekeeping;
CREATE DATABASE IF NOT EXISTS timekeeping DEFAULT CHARACTER SET utf8;
USE timekeeping;


-- -----------------------------------------------------
-- Table roles
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS roles
(
    PRIMARY KEY (id),
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL UNIQUE
);


-- -----------------------------------------------------
-- Table users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users
(
    PRIMARY KEY (id),
    id       INT          NOT NULL AUTO_INCREMENT,
    login    VARCHAR(32)  NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(32)  NOT NULL,
    role_id  INT          NOT NULL,
    FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table categories
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS categories
(
    PRIMARY KEY (id),
    id      INT         NOT NULL AUTO_INCREMENT,
    name_en VARCHAR(32) NOT NULL UNIQUE,
    name_uk VARCHAR(32) NOT NULL UNIQUE
);


-- -----------------------------------------------------
-- Table activities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS activities
(
    PRIMARY KEY (id),
    id          INT         NOT NULL AUTO_INCREMENT,
    name_en     VARCHAR(32) NOT NULL UNIQUE,
    name_uk     VARCHAR(32) NOT NULL UNIQUE,
    category_id INT         NOT NULL,
    FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table statuses
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS statuses
(
    PRIMARY KEY (id),
    id      INT         NOT NULL AUTO_INCREMENT,
    name_en VARCHAR(32) NOT NULL UNIQUE,
    name_uk VARCHAR(32) NOT NULL UNIQUE
);


-- -----------------------------------------------------
-- Table types
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS types
(
    PRIMARY KEY (id),
    id      INT         NOT NULL AUTO_INCREMENT,
    name_en VARCHAR(32) NOT NULL UNIQUE,
    name_uk VARCHAR(32) NOT NULL UNIQUE
);


-- -----------------------------------------------------
-- Table users_activities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users_activities
(
    PRIMARY KEY (user_id, activity_id),
    user_id     INT  NOT NULL,
    activity_id INT  NOT NULL,
    is_active   BOOL NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (activity_id)
        REFERENCES activities (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table intervals
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS intervals
(
    PRIMARY KEY (id),
    id          INT      NOT NULL AUTO_INCREMENT,
    start       DATETIME NULL,
    finish      DATETIME NULL,
    user_id     INT      NOT NULL,
    activity_id INT      NOT NULL,
    FOREIGN KEY (user_id, activity_id)
        REFERENCES users_activities (user_id, activity_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table requests
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS requests
(
    PRIMARY KEY (id),
    id          INT NOT NULL AUTO_INCREMENT,
    user_id     INT NOT NULL,
    activity_id INT NOT NULL,
    type_id     INT NOT NULL,
    status_id   INT NOT NULL,
    FOREIGN KEY (status_id)
        REFERENCES statuses (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (type_id)
        REFERENCES types (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (activity_id)
        REFERENCES activities (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- fill roles
-- -----------------------------------------------------
INSERT INTO roles
VALUES (DEFAULT, 'admin');
INSERT INTO roles
VALUES (DEFAULT, 'user');


-- -----------------------------------------------------
-- fill Users
-- -----------------------------------------------------
INSERT INTO users
VALUES (DEFAULT, 'admin', 'admin@test.com', 'admin', 1);
INSERT INTO users
VALUES (DEFAULT, 'petro', 'testpetro@test.com', '1111', 2);
INSERT INTO users
VALUES (DEFAULT, 'testUser', 'testUser@test.com', '2222', 2);
INSERT INTO users
VALUES (DEFAULT, 'ivan', 'testIvan@test.com', '3333', 2);


-- -----------------------------------------------------
-- fill statuses
-- -----------------------------------------------------
INSERT INTO statuses VALUES (DEFAULT, 'Pending...', 'В очікуванні...');
INSERT INTO statuses VALUES (DEFAULT, 'Approved', 'Одобрено');
INSERT INTO statuses VALUES (DEFAULT, 'Declined', 'Відхилено');


-- -----------------------------------------------------
-- fill types
-- -----------------------------------------------------
INSERT INTO types VALUES (DEFAULT, 'Add', 'Додати');
INSERT INTO types VALUES (DEFAULT, 'Remove', 'Видалити');


-- -----------------------------------------------------
-- fill categories of activities
-- -----------------------------------------------------
INSERT INTO categories VALUES (DEFAULT, 'Sport', 'Cпорт');
INSERT INTO categories VALUES (DEFAULT, 'Art', 'Мистецтво');


-- -----------------------------------------------------
-- fill activities
-- -----------------------------------------------------
INSERT INTO activities VALUES (DEFAULT, 'Swimming', 'Плавання', 1);
INSERT INTO activities VALUES (DEFAULT, 'Drawing', 'Малювання', 2);


-- -----------------------------------------------------
-- fill request
-- -----------------------------------------------------
INSERT INTO requests
VALUES (DEFAULT, 1, 1, 1, 1);
INSERT INTO requests
VALUES (DEFAULT, 1, 2, 1, 2);


-- -----------------------------------------------------
-- fill users_activities
-- -----------------------------------------------------
INSERT INTO users_activities VALUES (1, 1, TRUE);
INSERT INTO users_activities VALUES (1, 2, FALSE);
INSERT INTO users_activities VALUES (2, 1, FALSE);


-- -----------------------------------------------------
-- fill intervals
-- -----------------------------------------------------
INSERT INTO intervals
    VALUES (DEFAULT, CURRENT_TIMESTAMP, TIMESTAMP('2022-09-29 17:54:04'), 1, 1);
INSERT INTO intervals
    VALUES (DEFAULT, TIMESTAMP('2022-09-29 18:54:04'), TIMESTAMP('2022-09-29 18:54:04'), 1, 2);
