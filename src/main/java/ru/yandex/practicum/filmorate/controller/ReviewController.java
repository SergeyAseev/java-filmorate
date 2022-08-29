package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public Review findById(@PathVariable Long id) {
        log.info("Send get request /reviews/{}", id);
        return reviewService.findById(id);
    }

    @GetMapping
    public List<Review> findAllByFilmId(@RequestParam(required = false) Long filmId,
                                        @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Send get request /reviews?filmId={}&count={}", filmId, count);
        return reviewService.findAllByFilmId(filmId, count);
    }

    @PostMapping
    public Review save(@Valid @RequestBody Review review) {
        log.info("Send post request /reviews {}", review);
        return reviewService.save(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Send post request /reviews {}", review);
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Send delete request /reviews/{}", id);
        reviewService.deleteById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Send put request /reviews/{}/like/{}", id, userId);
        reviewService.setLike(id, userId, true);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void setDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Send put request /reviews/{}/dislike/{}", id, userId);
        reviewService.setDislike(id, userId, false);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Send put request /reviews/{}/like/{}", id, userId);
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Send put request /reviews/{}/dislike/{}", id, userId);
        reviewService.deleteDislike(id, userId);
    }
}
