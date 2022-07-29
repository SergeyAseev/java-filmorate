package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private  final FilmService filmService;

    @Autowired
    public GenreController(@Qualifier("FilmDbService") FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(value = "/{id}")
    public Genre retrieveGenreById(@PathVariable int id) {
        return filmService.retrieveGenreById(id);
    }

    @GetMapping
    public List<Genre> retrieveAllGenres() {
        return filmService.retrieveAllGenres();
    }
}
