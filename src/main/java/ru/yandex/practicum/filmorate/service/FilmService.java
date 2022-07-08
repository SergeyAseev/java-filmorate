package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    /**
     * Проставляем лайк фильму от пользователя
     */
    void addLike(int filmId, int userId);

    /**
     * Удаляем лайк у фильма от пользователя
     */
    void removeLike(int filmId, int userId);

    /**
     * Возвращаем отсортированный список фильмов по рейтингу
     * @param count число фильмов, которые надо вернуть
     * @return отсортированный список фильмов
     */
    List<Film> returnPopularFilms(int count);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void removeFilmById(int filmId);

    void removeAllFilms();

    Film retrieveFilmById(int filmId);

    List<Film> retrieveAllFilms();
}
