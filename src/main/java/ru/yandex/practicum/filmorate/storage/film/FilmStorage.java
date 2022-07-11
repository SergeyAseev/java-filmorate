package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

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
     * Удаляем все фильмы
     */
    void removeAllFilms();

    /**
     * Получаем фильм по ID
     *
     * @return экземпляр фильма
     */
    Optional<Film> retrieveFilmById(long filmId);

    /**
     * Получаем все фильмы
     * @return список всех значений LinkedHashMap, которая хранит все фильмы
     */
    List<Film> retrieveAllFilms();

    Map<Long, Film> retrieveFilms();

}
