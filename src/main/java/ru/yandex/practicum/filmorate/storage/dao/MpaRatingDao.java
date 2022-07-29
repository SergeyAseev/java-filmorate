package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaRatingDao {

    List<Mpa> retrieveAllMpaRatings();

    Optional<Mpa> retrieveMpaRatingById(int id);

    Mpa getFilmMpa(long id);
}
