package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.Collection;
import java.util.Set;

public interface LikesDao {

    Collection<Likes> getAllLikes();

    Set<Long> getFilmLikes(long filmId);
}
