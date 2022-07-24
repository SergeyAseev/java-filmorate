# java-filmorate
Ссылка на схему: https://dbdiagram.io/d/62dd153c0d66c746553aa527

SQL-запросы создания базы данных:

CREATE TABLE "user" (
"id" SERIAL PRIMARY KEY,
"email" varchar(50),
"name" varchar(50),
"login" varchar(10),
"birthday" date
);

CREATE TABLE "likes_links" (
"user_id" long,
"film_id" long
);

CREATE TABLE "film" (
"id" SERIAL PRIMARY KEY,
"name" varchar(100),
"description" varchar(text),
"releaseDate" date,
"duration" int
);

CREATE TABLE "friends" (
"from_user_id" long,
"to_user_id" long,
"status_friend_id" varchar
);

CREATE TABLE "film_genre_links" (
"film_id" long,
"genre_id" varchar
);

CREATE TABLE "genre" (
"id" varchar(5),
"name" varchar(20)
);

CREATE TABLE "friend_status" (
"id" varchar(5),
"name" varchar(20)
);

CREATE TABLE "film_rating_links" (
"film_id" long,
"rating_id" varchar
);

CREATE TABLE "rating" (
"id" varchar(5),
"name" varchar(20)
);

ALTER TABLE "likes_links" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "likes_links" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("from_user_id") REFERENCES "user" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("to_user_id") REFERENCES "user" ("id");

ALTER TABLE "film_genre_links" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "genre" ADD FOREIGN KEY ("id") REFERENCES "film_genre_links" ("genre_id");

ALTER TABLE "friend_status" ADD FOREIGN KEY ("id") REFERENCES "friends" ("status_friend_id");

ALTER TABLE "film_rating_links" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "rating" ADD FOREIGN KEY ("id") REFERENCES "film_rating_links" ("rating_id");
