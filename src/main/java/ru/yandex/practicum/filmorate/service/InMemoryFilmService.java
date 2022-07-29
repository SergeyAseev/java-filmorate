package ru.yandex.practicum.filmorate.service;
/*
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
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
                //filmStorage.getFilm(filmId).setRate(filmStorage.getFilm(filmId).getLikes().size());
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
                //filmStorage.getFilm(filmId).setRate(filmStorage.getFilm(filmId).getLikes().size());
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
    public List<Film> returnPopularFilms(long count) { throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public Film addFilm(Film film) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public Film updateFilm(Film film) { throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED); }

    @Override
    public void removeFilmById(long filmId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void removeAllFilms() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public Film retrieveFilmById(long filmId) { throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);}

    public List<Film> retrieveAllFilms() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public List<MpaRating> retrieveAllMpaRatings() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public MpaRating retrieveMpaRatingById(int id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public List<Genre> retrieveAllGenres() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public Genre retrieveGenreById(int id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}*/
