package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public interface GenreDao {

    List<Genre> retrieveAllGenres();

    Optional<Genre> retrieveGenreById(int id);

    List<Genre> getFilmGenres(long id);

    void fillingGenres(Film film);
}
