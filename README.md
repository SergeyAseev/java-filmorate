# java-filmorate

![Снимок базы](https://user-images.githubusercontent.com/23657190/180660600-8afb5d58-35bd-43e4-8334-10173a2508f9.PNG)

[Ссылка на схему](https://dbdiagram.io/d/62dd153c0d66c746553aa527)

Описание сущностей базы данных

Таблицы:

**film** - сущность **фильмы**
* id - ID фильма, первичный ключ
* name - название
* description - описание
* release_date - дата выхода
* duration - длительность фильма
* mpa_rating_id - внешний ключ на таблицу mpa_rating

**mpa_rating** - сущность **рейтинги фильмов**
* id - ID, первичный ключ
* name - название рейтинга

**film_genre_links** - промежуточная сущность для связи многие ко многим
* film_id - внешний ключ, ссылка на таблицу фильмов
* genre_id - внешний ключ, ссылка на таблицу жанров

**genre** - сущность **жанры фильмов**
* id - ID, первичный ключ
* name - название жанра

**sec_user** - сущность **пользователь**
* id - ID, первичный ключ
* email - электронная почта
* login - логин пользователя
* name - имя пользователя
* birthday - дата рождения

**likes_links** - промежуточная сущность для связи многие ко многим
* film_id - внешний ключ, ссылка на таблицу фильмов
* user_id - внешний ключ, ссылка на таблицу пользователей

**friends** - сущность **дружба между пользователями**
* from_user_id - пользователь, кто запросил
* to_user_id - пользователь, к кому запросили
* status_friend_id - внешний ключ на таблицу friend_status

**friend_status** - сущность **статус дружбы между пользователями**
* id - ID, первичный ключ
* name - название статуса дружбы

**Некоторые скрипты SQL**

**1) Получаем популярные фильмы**

    SELECT 
      f.name, 
      f.description, 
      f.release_date, 
      f.duration,
      count (ll.user_id) as likesCount
    FROM film f
      LEFT JOIN likes_links ll 
      ON f.id = ll.film_id
    GROUP BY f.name, f.description, f.release_date, f.duration
    ORDER BY COUNT likesCount DESC
    LIMIT ?;

**2) Получаем друзей пользователя по его id**

    SELECT 
      u.login, 
      u.email, 
      u.name
    FROM users u
      LEFT JOIN friends fr 
      ON u.id = fr.to_user_id
    WHERE fr.from_user_id = ?;
