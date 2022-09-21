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
INSERT INTO roles VALUES (DEFAULT, 'admin');
INSERT INTO roles VALUES (DEFAULT, 'user');


-- -----------------------------------------------------
-- fill Users
-- -----------------------------------------------------
INSERT INTO users VALUES (DEFAULT, 'admin', 'admin@test.com', 'Admin123', 1);
INSERT INTO users VALUES (DEFAULT, 'petro', 'testpetro@test.com', 'Petro000', 2);
INSERT INTO users VALUES (DEFAULT, 'testUser', 'testUser@test.com', 'testPassword1', 2);
INSERT INTO users VALUES (DEFAULT, 'ivan', 'testIvan@test.com', 'ivaN3333', 2);


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
INSERT INTO categories VALUES (DEFAULT, 'Team-Building Activities', 'Заходи з формування команди');
INSERT INTO categories VALUES (DEFAULT, 'Work', 'Робота');
INSERT INTO categories VALUES (DEFAULT, 'Education', 'Навчання');
INSERT INTO categories VALUES (DEFAULT, 'Vacation', 'Відпустка');
INSERT INTO categories VALUES (DEFAULT, 'Advertisement', 'Реклама');


-- -----------------------------------------------------
-- fill activities
-- -----------------------------------------------------
INSERT INTO activities VALUES (DEFAULT, 'Swimming', 'Плавання', 1);
INSERT INTO activities VALUES (DEFAULT, 'running', 'біг', 1);
INSERT INTO activities VALUES (DEFAULT, 'athletics', 'атлетика', 1);
INSERT INTO activities VALUES (DEFAULT, 'walking', 'прогулянки', 1);
INSERT INTO activities VALUES (DEFAULT, 'cycling', 'їзда на велосипеді', 1);
INSERT INTO activities VALUES (DEFAULT, 'yoga', 'йога', 1);
INSERT INTO activities VALUES (DEFAULT, 'golf', 'гольф', 1);
INSERT INTO activities VALUES (DEFAULT, 'tennis', 'теніс', 1);
INSERT INTO activities VALUES (DEFAULT, 'football', 'футбол', 1);
INSERT INTO activities VALUES (DEFAULT, 'Drawing', 'Малювання', 2);
INSERT INTO activities VALUES (DEFAULT, 'Pan art', 'Панорамне мистецтво', 2);
INSERT INTO activities VALUES (DEFAULT, 'Creating a nature collage', 'Створення природного колажу', 2);
INSERT INTO activities VALUES (DEFAULT, 'Splatter painting', 'Малювання бризками', 2);
INSERT INTO activities VALUES (DEFAULT, 'Solving a puzzle', 'Розгадування головоломки', 3);
INSERT INTO activities VALUES (DEFAULT, 'Brainstorming Session', 'Мозковий штурм', 3);
INSERT INTO activities VALUES (DEFAULT, 'Sharing your personality', 'Ділитися своєю індивідуальністю', 3);
INSERT INTO activities VALUES (DEFAULT, 'Playing Team Games', 'Командні ігри', 3);
INSERT INTO activities VALUES (DEFAULT, 'Untangle a Human Knot', 'Розплутування людського вузла', 3);


-- -----------------------------------------------------
-- fill request
-- -----------------------------------------------------
INSERT INTO requests VALUES (DEFAULT, 2, 1, 1, 1);
INSERT INTO requests VALUES (DEFAULT, 2, 2, 1, 1);
INSERT INTO requests VALUES (DEFAULT, 3, 1, 1, 1);
INSERT INTO requests VALUES (DEFAULT, 3, 2, 1, 1);
INSERT INTO requests VALUES (DEFAULT, 4, 1, 1, 1);
INSERT INTO requests VALUES (DEFAULT, 4, 2, 1, 1);

INSERT INTO requests VALUES (DEFAULT, 2, 3, 1, 3);
INSERT INTO requests VALUES (DEFAULT, 2, 4, 1, 3);
INSERT INTO requests VALUES (DEFAULT, 3, 3, 1, 3);
INSERT INTO requests VALUES (DEFAULT, 3, 4, 1, 3);
INSERT INTO requests VALUES (DEFAULT, 4, 3, 1, 3);
INSERT INTO requests VALUES (DEFAULT, 4, 4, 1, 3);

INSERT INTO requests VALUES (DEFAULT, 2, 5, 1, 2);
INSERT INTO requests VALUES (DEFAULT, 2, 6, 1, 2);
INSERT INTO requests VALUES (DEFAULT, 3, 5, 1, 2);
INSERT INTO requests VALUES (DEFAULT, 3, 6, 1, 2);
INSERT INTO requests VALUES (DEFAULT, 4, 5, 1, 2);
INSERT INTO requests VALUES (DEFAULT, 4, 6, 1, 2);

INSERT INTO requests VALUES (DEFAULT, 2, 5, 2, 3);
INSERT INTO requests VALUES (DEFAULT, 2, 6, 2, 3);
INSERT INTO requests VALUES (DEFAULT, 3, 5, 2, 3);
INSERT INTO requests VALUES (DEFAULT, 3, 6, 2, 3);
INSERT INTO requests VALUES (DEFAULT, 4, 5, 2, 3);
INSERT INTO requests VALUES (DEFAULT, 4, 6, 2, 3);


-- -----------------------------------------------------
-- fill users_activities
-- -----------------------------------------------------
INSERT INTO users_activities VALUES (2, 5, TRUE);
INSERT INTO users_activities VALUES (2, 6, DEFAULT);
INSERT INTO users_activities VALUES (3, 5, DEFAULT);
INSERT INTO users_activities VALUES (3, 6, TRUE);
INSERT INTO users_activities VALUES (4, 5, TRUE);
INSERT INTO users_activities VALUES (4, 6, DEFAULT);


-- -----------------------------------------------------
-- fill intervals
-- -----------------------------------------------------
INSERT INTO intervals VALUES (DEFAULT, '2022-06-25 10:46:11', '2022-06-30 18:46:11', 2, 5);
INSERT INTO intervals VALUES (DEFAULT, '2022-06-24 10:46:11', '2022-06-30 18:46:11', 2, 6);
INSERT INTO intervals VALUES (DEFAULT, '2022-06-23 10:46:11', '2022-06-30 18:46:11', 3, 5);
INSERT INTO intervals VALUES (DEFAULT, '2022-06-22 10:46:11', '2022-06-30 18:46:11', 3, 6);
INSERT INTO intervals VALUES (DEFAULT, '2022-08-21 13:46:11', '2022-08-30 23:46:11', 4, 5);
INSERT INTO intervals VALUES (DEFAULT, '2022-06-20 10:46:11', '2022-06-30 18:46:11', 4, 6);
INSERT INTO intervals VALUES (DEFAULT, '2022-09-18 23:46:11', NULL, 2, 5);
INSERT INTO intervals VALUES (DEFAULT, NULL, NULL, 2, 6);
INSERT INTO intervals VALUES (DEFAULT, NULL, NULL, 3, 5);
INSERT INTO intervals VALUES (DEFAULT, '2022-09-18 10:46:11', NULL, 3, 6);
INSERT INTO intervals VALUES (DEFAULT, '2022-09-20 23:40:11', NULL, 4, 5);
INSERT INTO intervals VALUES (DEFAULT, NULL, NULL, 4, 6);
