package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {

    private final FilmService filmService;

    @Autowired
    public MpaRatingController (@Qualifier("FilmDbService") FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(value = "/{id}")
    public Mpa retrieveMpaRatingById(@PathVariable int id) {
        return filmService.retrieveMpaRatingById(id);
    }

    @GetMapping
    public List<Mpa> retrieveAllMpaRatings() {
        return filmService.retrieveAllMpaRatings();
    }
}
