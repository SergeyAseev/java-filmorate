package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    /**
     * Проставляем лайк фильму от пользователя
     */
    void addLike(long filmId, long userId);

    /**
     * Удаляем лайк у фильма от пользователя
     */
    void removeLike(long filmId, long userId);

    /**
     * Возвращаем отсортированный список фильмов по рейтингу
     * @param count число фильмов, которые надо вернуть
     * @return отсортированный список фильмов
     */
    List<Film> returnPopularFilms(long count);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void removeFilmById(long filmId);

    void removeAllFilms();

    Film retrieveFilmById(long filmId);

    List<Film> retrieveAllFilms();
}
