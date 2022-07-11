package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage,
                               @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(long filmId, long userId) {
        if (filmStorage.retrieveFilms().containsKey(filmId)) {
            if (!retrieveFilmById(filmId).getLikes().contains(userId)) {
                retrieveFilmById(filmId).getLikes().add(userId);
                log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
            } else {
                throw new ValidationException(String.format("Пользователь c ID %s уже " +
                        "оценивал фильм с ID %s", userId, filmId));
            }
        } else {
            throw new NotFoundException(String.format("Нет фильма с ID %s", filmId));
        }
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (filmStorage.retrieveFilms().containsKey(filmId)) {
            if (retrieveFilmById(filmId).getLikes().contains(userId)) {
                retrieveFilmById(filmId).getLikes().remove(userId);
                log.info("Пользователь с ID {} удалил лайк у фильма с ID {}", userId, filmId);
            } else {
                throw new NotFoundException(String.format("Пользователь c ID %s не " +
                        "оценивал фильм с ID %s", userId, filmId));
            }
        } else {
            throw new NotFoundException(String.format("Нет фильма с ID %s", filmId));
        }
    }

    @Override
    public List<Film> returnPopularFilms(long count) {
        return filmStorage.retrieveAllFilms().stream()
                .sorted(Comparator.comparing(e-> e.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        retrieveFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    @Override
    public void removeFilmById(long filmId) {
        filmStorage.removeFilmById(filmId);
    }

    @Override
    public void removeAllFilms() {
        filmStorage.removeAllFilms();
    }

    @Override
    public Film retrieveFilmById(long filmId) {
         return filmStorage.retrieveFilmById(filmId).orElseThrow(() ->
                new NotFoundException(String.format("Не найден фильм с ID %s", filmId)));
    }

    public List<Film> retrieveAllFilms() {
        return filmStorage.retrieveAllFilms();
    }
}
