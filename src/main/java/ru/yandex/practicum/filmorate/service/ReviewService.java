package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDao;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final UserService userService;
    private final FilmService filmService;


    public Review findById(Long id) {
        try {
            return reviewDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Отзыв по id=%s не найден!", id), e);
        }
    }

    public List<Review> findAllByFilmId(Long filmId, Integer count) {
        return reviewDao.findAllByFilmId(filmId, count);
    }

    public Review save(Review review) {
        final User user = userService.retrieveUserById(review.getUserId());
        final Film film = filmService.retrieveFilmById(review.getFilmId());

        if (user == null || film == null) {
            throw new NotFoundException(String.format("Сущность не найдена! userId=%s filmId=%s"
                    , user.getId()
                    , film.getId())
            );
        }
        return reviewDao.save(review);
    }

    public Review update(Review review) {
        return reviewDao.update(review);
    }

    public void deleteById(Long id) {
        reviewDao.deleteById(id);
    }

    public void setLike(Long id, Long userId, Boolean isPositive) {
        reviewDao.setLike(id, userId, isPositive);
    }

    public void setDislike(Long id, Long userId, Boolean isPositive) {
        reviewDao.setDislike(id, userId, isPositive);
    }

    public void deleteLike(Long id, Long userId) {
        reviewDao.deleteLike(id, userId);
    }

    public void deleteDislike(Long id, Long userId) {
        reviewDao.deleteDislike(id, userId);
    }
}
