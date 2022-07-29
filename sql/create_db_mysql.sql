-- -----------------------------------------------------
-- Schema timekeeping
-- -----------------------------------------------------
DROP DATABASE IF EXISTS timekeeping;
CREATE DATABASE IF NOT EXISTS timekeeping DEFAULT CHARACTER SET utf8;
USE timekeeping;
-- DROP TABLE IF EXISTS roles;.

-- -----------------------------------------------------
-- Table roles
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS roles
(
    PRIMARY KEY (id),
    id           INT         NOT NULL AUTO_INCREMENT,
    name         VARCHAR(32) NOT NULL,
    created_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- -----------------------------------------------------
-- Table users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users
(
    PRIMARY KEY (id),
    id           INT          NOT NULL AUTO_INCREMENT,
    login        VARCHAR(32)  NOT NULL,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(32)  NOT NULL,
    created_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role_id      INT          NOT NULL,
    FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table languages
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS languages
(
    PRIMARY KEY (id),
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL
);


-- -----------------------------------------------------
-- Table translations
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS translations
(
    PRIMARY KEY (id),
    id          INT         NOT NULL AUTO_INCREMENT,
    translated  VARCHAR(32) NOT NULL,
    language_id INT         NOT NULL,
    FOREIGN KEY (language_id)
        REFERENCES languages (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table categories
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS categories
(
    PRIMARY KEY (id),
    id             INT         NOT NULL AUTO_INCREMENT,
    name           VARCHAR(32) NOT NULL,
    translation_id INT         NOT NULL,
    created_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (translation_id)
        REFERENCES translations (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table activities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS activities
(
    PRIMARY KEY (id),
    id             INT         NOT NULL AUTO_INCREMENT,
    name           VARCHAR(32) NOT NULL,
    translation_id INT         NOT NULL,
    category_id    INT         NOT NULL,
    created_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id)
        REFERENCES categories (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    FOREIGN KEY (translation_id)
        REFERENCES translations (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table statuses
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS statuses
(
    PRIMARY KEY (id),
    id             INT         NOT NULL AUTO_INCREMENT,
    name           VARCHAR(32) NOT NULL,
    translation_id INT         NOT NULL,
    FOREIGN KEY (translation_id)
        REFERENCES translations (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table types
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS types
(
    PRIMARY KEY (id),
    id             INT         NOT NULL AUTO_INCREMENT,
    name           VARCHAR(32) NOT NULL,
    translation_id INT         NOT NULL,
    FOREIGN KEY (translation_id)
        REFERENCES translations (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table intervals
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users_activities
(
    PRIMARY KEY (user_id, activity_id),
    user_id     INT                 NOT NULL,
    activity_id INT                 NOT NULL,
    interval_id INT                 NOT NULL,
    is_active   TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table users_activities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS intervals
(
    PRIMARY KEY (id),
    id           INT      NOT NULL AUTO_INCREMENT,
    started      DATETIME NULL,
    finish       DATETIME NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id      INT      NOT NULL,
    activity_id  INT      NOT NULL,
    FOREIGN KEY (user_id, activity_id)
        REFERENCES users_activities (user_id, activity_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table requests
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS requests
(
    PRIMARY KEY (id),
    id           INT      NOT NULL,
    user_id      INT      NOT NULL,
    activity_id  INT      NOT NULL,
    type_id      INT      NOT NULL,
    status_id    INT      NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (status_id)
        REFERENCES statuses (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    FOREIGN KEY (type_id)
        REFERENCES types (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    FOREIGN KEY (user_id, activity_id)
        REFERENCES users_activities (user_id, activity_id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);