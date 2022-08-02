package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    /**
     * Получаем все жанры
     * @return список значений, который хранит все жанры
     */
    List<Genre> retrieveAllGenres();

    /**
     * Получаем жанр по ID
     * @return экземпляр жанра
     */
    Genre retrieveGenreById(int id);
}
