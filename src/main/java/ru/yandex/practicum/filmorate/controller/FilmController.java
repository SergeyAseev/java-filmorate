package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    protected int nextFilmId = 0;
    private final Map<Integer, Film> films = new LinkedHashMap<Integer, Film>();

    /**
     * Получаем все фильмы
     * @return список всех значений LinkedHashMap, которая хранит все фильмы
     */
    @GetMapping("films")
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    /**
     * Создаем новый фильм
     * @param film экземпляр текущего фильма в виде Json
     * @return экземпляр созданного фильма
     */
    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм c ID {} успешно добавлен", film.getId());
        return film;
    }

    /**
     * Обновляем данные уже существующего фильма
     * @param film экземпляр текущего фильма
     * @return экземпляр обновленного фильма
     */
    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            RuntimeException runtimeException = new ValidationException("Не найден фильм с таким ID");
            log.info(runtimeException.getMessage());
            throw runtimeException;
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Фильм c ID {} успешно обновлен", film.getId());
        return film;
    }

    /**
     * Проверка создаваемого фильма на валидность
     * @param film экземпляр текущего фильма
     */
    protected void validate(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Название должно быть менее 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Релиз не может быть раньше 28 декабря 1895");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть числом положительным");
        }
    }

    /**
     * увеличивает уникальный идентификатор фильма
     */
    protected int generateId() {
        return ++nextFilmId;
    }
}
