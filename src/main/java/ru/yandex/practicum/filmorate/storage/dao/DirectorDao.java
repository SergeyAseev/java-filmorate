package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {

    /**
     * Получение списка всех режиссеров
     * @return
     */
    List<Director> findAll ();

    /**
     * Получение режиссера по id
     * @param id
     * @return
     */
    Director findDirectorById (Integer id);

    /**
     * Создание режиссера
     * @param director
     * @return
     */
    Director createDirector (Director director);

    /**
     * Изменение режиссера
     * @param director
     * @return
     */
    Director updateDirector (Director director);

    /**
     * Удаление режиссера
     * @param id
     * @return
     */
    void removeDirector (Integer id);

    /**
     * Получить режисеров для фильма по id
     * @param id
     * @return
     */
    List<Director> getDirectorByFilmID(Long id);
}
