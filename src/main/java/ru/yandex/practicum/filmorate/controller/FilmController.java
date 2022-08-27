package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController (@Qualifier("FilmDbService")FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> retrieveAllFilms() {
        return filmService.retrieveAllFilms();
    }


    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        filmService.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping(value = "/{id}")
    public Film retrieveFilmById(@PathVariable long id) {
        return filmService.retrieveFilmById(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}")
    public void removeFilmById(@PathVariable long id) {
        filmService.removeFilmById(id);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLikeToFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.returnPopularFilms(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> findSortFilmsByDirector(@PathVariable Integer directorId,
                                                         @RequestParam String sortBy) {
        return filmService.findSortFilmsByDirector(directorId, sortBy);
    }

}
