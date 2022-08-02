package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    /**
     * Получаем жанры из БД
     * @return список жанров
     */
    List<Genre> retrieveAllGenres();

    /**
     *
     * @param genreId идентификатор жанра
     * @return экземпляр сущности Genre
     */
    Optional<Genre> retrieveGenreById(int genreId);

    /**
     * Получаем жанры фильма
     * @param filmId идентификатор фильма
     * @return список жанров фильма
     */
    List<Genre> getFilmGenres(long filmId);

    /**
     * Заполяем промежуточную таблицу-связи фильма и жанра
     * @param film экземпляр фильма
     */
    void fillInGenres(Film film);
}
