package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.LikesLinks;

import java.util.Collection;
import java.util.Set;

public interface LikesLinksDao {

    Collection<LikesLinks> getAllLikes();

    Set<Long> getFilmLikes(long filmId);
}
