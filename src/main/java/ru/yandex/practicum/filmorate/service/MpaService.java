package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaService {

    /**
     * Получаем все рейтинги
     * @return список значений, который хранит все рейтинги
     */
    List<Mpa> retrieveAllMpaRatings();

    /**
     * Получаем рейтинг по ID
     * @return экземпляр рейтинга
     */
    Mpa retrieveMpaRatingById(int id);
}
