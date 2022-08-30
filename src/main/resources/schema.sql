CREATE TABLE IF NOT EXISTS USERS
(
    ID       LONG NOT NULL AUTO_INCREMENT,
    LOGIN    CHARACTER VARYING(50) NOT NULL,
    EMAIL    CHARACTER VARYING(50) NOT NULL,
    NAME     CHARACTER VARYING(100),
    BIRTHDAY DATE,
    CONSTRAINT USERS_PK
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS MPA
(
    ID INT NOT NULL,
    NAME   CHARACTER VARYING(20) NOT NULL,
    CONSTRAINT MPA_PK
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    ID           LONG NOT NULL AUTO_INCREMENT,
    NAME         CHARACTER VARYING(100) NOT NULL,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INT,
    MPA_ID INT,
    PRIMARY KEY (ID),
    CONSTRAINT FILMS_ID_MPA_FK
    FOREIGN KEY (MPA_ID) REFERENCES MPA (ID)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    FILM_ID LONG NOT NULL,
    USER_ID LONG NOT NULL,
    CONSTRAINT LIKES_PK
    PRIMARY KEY (FILM_ID, USER_ID),
    CONSTRAINT LIKES_USER_ID_FK
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    CONSTRAINT LIKES_FILM_ID_FK
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    FROM_USER LONG NOT NULL,
    TO_USER   LONG NOT NULL,
    CONSTRAINT FRIENDS_PK
    PRIMARY KEY (FROM_USER, TO_USER),
    CONSTRAINT FROM_USER_FK
    FOREIGN KEY (FROM_USER) REFERENCES USERS (ID) ON DELETE CASCADE,
    CONSTRAINT TO_USER_FK
    FOREIGN KEY (TO_USER) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS GENRE
(
    ID INT NOT NULL,
    NAME CHARACTER VARYING(20) NOT NULL,
    CONSTRAINT GENRE_PK
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE_LINKS
(
    FILM_ID  LONG NOT NULL,
    GENRE_ID INT NOT NULL,
    CONSTRAINT FILM_GENRE_PK
    PRIMARY KEY (FILM_ID, GENRE_ID),
    CONSTRAINT GENRE_FILM_ID_FK
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (ID) ON DELETE CASCADE,
    CONSTRAINT GENRE_ID_FK
    FOREIGN KEY (GENRE_ID) REFERENCES GENRE (ID)
    );

CREATE TABLE IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    DIRECTOR_NAME VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS FILMS_DIRECTORS
(
  FILM_ID LONG,
  DIRECTOR_ID INTEGER,
  constraint DIRECTOR_ID
      foreign key (DIRECTOR_ID) REFERENCES DIRECTORS,
  constraint FILM_ID_DIR
      foreign key (FILM_ID) REFERENCES FILMS
);

CREATE TABLE IF NOT EXISTS REVIEWS (
    ID LONG NOT NULL AUTO_INCREMENT,
    CONTENT CHARACTER VARYING(200) NOT NULL,
    IS_POSITIVE BOOLEAN NOT NULL,
    USER_ID LONG,
    FILM_ID LONG,
    USEFUL LONG,
    PRIMARY KEY (ID),
    CONSTRAINT REVIEWS_ID_USER_FK
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
    CONSTRAINT REVIEWS_ID_FILM_FK
    FOREIGN KEY (FILM_ID) REFERENCES FILMS (ID)
);
