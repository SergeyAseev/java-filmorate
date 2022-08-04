package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaRatingDao {

    /**
     * Получаем мпа-рейтинги из бд
     * @return все мпа-рейтинги
     */
    List<Mpa> retrieveAllMpaRatings();

    /**
     * Получаем мпа-рейтинг
     * @param mpaId иденификатор рейтинга
     * @return экзепляр сущности Мпа
     */
    Optional<Mpa> retrieveMpaRatingById(int mpaId);

    /**
     * Получаем мпа-рейтинг фильма
     * @param filmId идентификатор фильма
     * @return экзепляр сущности Мпа
     */
    Mpa getFilmMpa(long filmId);
}
