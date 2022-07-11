package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long filmId = 0;
    private final Map<Long, Film> films = new LinkedHashMap<Long, Film>();

    /**
     * увеличивает уникальный идентификатор фильма
     */
    protected long increaseId() {
        return ++filmId;
    }

    @Override
    public Film addFilm(Film film) {
        validate(film);
        film.setId(increaseId());
        films.put(film.getId(), film);
        log.info("Фильм c ID {} успешно добавлен", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        films.put(film.getId(), film);
        log.info("Фильм c ID {} успешно обновлен", film.getId());
        return film;
    }

    @Override
    public void removeFilmById(long filmId) {
        if (!films.containsKey(filmId)) {
            return;
        }
        films.remove(filmId);
        log.info("Фильм c ID {} успешно удален", filmId);
    }

    @Override
    public void removeAllFilms() {
        films.clear();
        log.info("Все фильмы удалены");
    }

    @Override
    public Optional<Film> retrieveFilmById(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public List<Film> retrieveAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Map<Long, Film> retrieveFilms() {
        return films;
    }

    /**
     * Проверка создаваемого фильма на валидность
     * @param film экземпляр текущего фильма
     */
    public void validate(Film film) {
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
}
