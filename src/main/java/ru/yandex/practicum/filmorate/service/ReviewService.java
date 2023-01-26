package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.dao.FeedDao;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDao;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final UserService userService;
    private final FilmService filmService;

    private final FeedDao feedDao;

    public Review findById(Long id) {
        return reviewDao.findById(id);
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
        Review newReview = reviewDao.save(review);
        feedDao.addFeed(newReview.getUserId(), EventEnum.REVIEW, OperationEnum.ADD, newReview.getReviewId());
        return newReview;
    }

    public Review update(Review review) {
        Review updatedReview = reviewDao.update(review);
        feedDao.addFeed(updatedReview.getUserId(), EventEnum.REVIEW, OperationEnum.UPDATE, updatedReview.getReviewId());
        return updatedReview;
    }

    public void deleteById(Long id) {
        feedDao.addFeed(findById(id).getReviewId(), EventEnum.REVIEW, OperationEnum.REMOVE, id);
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
