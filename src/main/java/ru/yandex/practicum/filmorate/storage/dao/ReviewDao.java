package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewDao {
    Review findById(Long id);
    List<Review> findAll();
    List<Review> findAllByFilmId(Long filmId, Integer count);
    Review save(Review review);
    Review update(Review review);
    void deleteById(Long id);
    void setLike(Long id, Long userId, Boolean isPositive);
    void setDislike(Long id, Long userId, Boolean isPositive);
    void deleteLike(Long id, Long userId);
    void deleteDislike(Long id, Long userId);
}
