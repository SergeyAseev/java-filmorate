package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController (FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> retrieveAllFilms() {
        return filmService.retrieveAllFilms();
    }


    @PostMapping
    @Valid
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }


    @PutMapping
    @Valid
    public Film updateFilm(@RequestBody Film film) {
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
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") long count) {
        return filmService.returnPopularFilms(count);
    }

}
