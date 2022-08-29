package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {

    /**
     * Получаем режиссеров из БД
     * @return список всех режиссеров
     */
    List<Director> findAll ();

    /**
     * Получение режиссера по id
     * @param id идентификатор режиссера
     * @return экземпляр сущности Director
     */
    Director findDirectorById (Integer id);

    /**
     * Создание режиссера
     * @param director экземпляр текущего режиссера
     * @return экземпляр созданного режиссера
     */
    Director createDirector (Director director);

    /**
     * Обновляем данные уже существующего режиссера
     * @param director экземпляр текущего режиссера
     * @return экземпляр обновленного режиссера
     */
    Director updateDirector (Director director);

    /**
     * Удаление режиссера
     * @param id идентификатор режиссера
     */
    void removeDirector (Integer id);

    /**
     * Получить список режисеров для фильма по id
     * @param id идентификатор фильма
     * @return список режиссеров
     */
    List<Director> getFilmDirectors(Long id);
}
