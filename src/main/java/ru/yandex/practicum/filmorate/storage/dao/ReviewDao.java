package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewDao {
    /**
     * Поиск ревью по полю id
     * @param id идентификатор ревью
     * @return экземпляр ревью
     */
    Review findById(Long id);

    /**
     * Поиск всех ревью с сортировкой по полю useful
     * @return список всех ревью
     */
    List<Review> findAll();

    /**
     * Поиск ревью по filmId с сортировкой по полю useful, постраничный вывод задается параметром count
     * @param filmId идентификатор фильма
     * @param count количество ревью
     * @return список всех ревью по filmId
     */
    List<Review> findAllByFilmId(Long filmId, Integer count);

    /**
     * Создание нового экземпляра ревью
     * @param review экземпляр ревью
     * @return экземпляр ревью
     */
    Review save(Review review);

    /**
     * Обновление экземпляра ревью
     * @param review экземпляр ревью
     * @return экземпляр ревью
     */
    Review update(Review review);

    /**
     * Удаление ревью по его id
     * @param id идентификатор ревью
     */
    void deleteById(Long id);

    /**
     * Пользователь ставит лайк ревью, isPositive = true
     * @param id идентификатор ревью
     * @param userId идентификатор пользователя
     * @param isPositive параметр отвечающий за лайки и дизлайки, принимает boolean.
     */
    void setLike(Long id, Long userId, Boolean isPositive);

    /**
     * Пользователь ставит дизлайк ревью, isPositive = false
     * @param id идентификатор ревью
     * @param userId идентификатор пользователя
     * @param isPositive параметр отвечающий за лайки и дизлайки, принимает boolean.
     */
    void setDislike(Long id, Long userId, Boolean isPositive);

    /**
     * Пользователь удаляет лайк ревью, isPositive = null
     * @param id идентификатор ревью
     * @param userId идентификатор пользователя
     */
    void deleteLike(Long id, Long userId);

    /**
     * Пользователь удаляет дизлайк ревью, isPositive = null
     * @param id идентификатор ревью
     * @param userId идентификатор пользователя
     */
    void deleteDislike(Long id, Long userId);
}
