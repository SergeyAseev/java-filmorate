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
    List<Film> returnPopularFilms(int count);

    /**
     * Создаем новый фильм
     * @param film экземпляр текущего фильма в виде Json
     * @return экземпляр созданного фильма
     */
    Film addFilm(Film film);

    /**
     * Обновляем данные уже существующего фильма
     * @param film экземпляр текущего фильма
     * @return экземпляр обновленного фильма
     */
    Film updateFilm(Film film);

    /**
     * Удаляем фильм по ID
     */
    void removeFilmById(long filmId);

    /**
     * Получаем фильм по ID
     * @return экземпляр фильма
     */
    Film retrieveFilmById(long filmId);

    /**
     * Получаем все фильмы
     * @return список значений, который хранит все фильмы
     */
    List<Film> retrieveAllFilms();
}
