package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.Collection;
import java.util.Set;

public interface LikesDao {

    /**
     *  Получаем все лайки из БД. P.s. не знаю зачем, но пусть будет)
     * @return
     */
    Collection<Likes> getAllLikes();

    /**
     *  Получаем список идентификаторов пользователей, который поставили лайк фильма
     * @param filmId идентификатор фильма
     * @return список ID пользователей
     */
    Set<Long> getFilmLikes(long filmId);
}
