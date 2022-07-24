# java-filmorate

![image](https://github.com/SergeyAseev/java-filmorate/files/9176352/filmorate.pdf)

[Ссылка на схему](https://dbdiagram.io/d/62dd153c0d66c746553aa527)

Описание сущностей базы данных

Таблицы:
--start film
**film** - сущность фильмы
id - ID фильма, первичный ключ
name - название
description - описание
release_date - дата выхода
duration - длительность фильма
mpa_rating_id - внешний ключ на таблицу mpa_rating
--end film

--start mpa_rating
**mpa_rating** - сущность рейтинги фильмов
id - ID, первичный ключ
name - название рейтинга
--end mpa_rating

--start film_genre_links
**film_genre_links** - сущность для связи многие ко многим
film_id - внешний ключ, ссылка на таблицу фильмов
genre_id - внешний ключ, ссылка на таблицу жанров
--end film_genre_links

--start genre
**genre** - сущность жанры фильмов
id - ID, первичный ключ
name - название жанра
--end genre

--start sec_user
**sec_user** - сущность пользователь
id - ID, первичный ключ
email - электронная почта
login - логин пользователя
name - имя пользователя
birthday - дата рождения
--end sec_user

--start likes_links
**likes_links** - сущность для связи многие ко многим
film_id - внешний ключ, ссылка на таблицу фильмов
user_id - внешний ключ, ссылка на таблицу пользователей
--end likes_links

--start friends
**friends** - сущность дружба между пользователями
from_user_id - пользователь, кто запросил
to_user_id - пользователь, к кому запросили
status_friend_id - внешний ключ на таблицу friend_status
--end friends

--start friend_status
**friend_status** - сущность дружба между пользователями
id - ID, первичный ключ
name - название статуса дружбы
--end friend_status
