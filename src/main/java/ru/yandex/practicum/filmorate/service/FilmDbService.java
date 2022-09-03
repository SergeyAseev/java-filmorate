package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("FilmDbService")
public class FilmDbService implements FilmService, MpaService, GenreService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreDao genreDao;
    private final MpaRatingDao mpaRatingDao;

    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         @Qualifier("UserDbStorage") UserStorage userStorage,
                         GenreDao genreDao,
                         MpaRatingDao mpaRatingDao
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaRatingDao = mpaRatingDao;
    }

    @Override
    public Film addFilm(Film film) {
        validate(film);
        filmStorage.addFilm(film);
        genreDao.fillInGenres(film);
        log.info("Добавлен фильм id = {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        retrieveFilmById(film.getId());
        genreDao.fillInGenres(film);
        filmStorage.updateFilm(film);
        log.info("Обновлен фильм id = {}", film.getId());
        return retrieveFilmById(film.getId());
    }

    @Override
    public void removeFilmById(long filmId) {
        filmStorage.removeFilmById(filmId);
        log.info("Удален фильм id = {}", filmId);
    }

    @Override
    public Film retrieveFilmById(long filmId) {
        return filmStorage.retrieveFilmById(filmId).orElseThrow(() ->
                new NotFoundException(String.format("Не найден фильм с ID %s", filmId)));
    }

    @Override
    public List<Film> retrieveAllFilms() {
        log.info("Возвращаем все фильмы");
        return filmStorage.retrieveAllFilms();
    }

    @Override
    public List<Mpa> retrieveAllMpaRatings() {
        log.info("Возвращаем все рейтинги");
        return mpaRatingDao.retrieveAllMpaRatings();
    }

    @Override
    public Mpa retrieveMpaRatingById(int ratingId) {
        log.info("Возвращаем рейтинг с ID = {}", ratingId);
        return mpaRatingDao.retrieveMpaRatingById(ratingId).orElseThrow(() ->
                new NotFoundException(String.format("Рейтинг с id %d не найден", ratingId)));
    }

    @Override
    public List<Genre> retrieveAllGenres() {
        log.info("Возвращаем все жанры");
        return genreDao.retrieveAllGenres();
    }

    @Override
    public Genre retrieveGenreById(int genreId) {
        log.info("Возвращаем жанр с ID = {}", genreId);
        return genreDao.retrieveGenreById(genreId).orElseThrow(() ->
                new NotFoundException(String.format("Жанр с id %d не найден", genreId)));
    }

    @Override
    public void addLike(long filmId, long userId) {
        if (filmStorage.retrieveFilmById(filmId).isPresent()) {
            if (!retrieveFilmById(filmId).getLikes().contains(userId)) {
                filmStorage.addLike(filmId, userId);
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
        if (filmStorage.retrieveFilmById(filmId).isPresent()) {
            if (retrieveFilmById(filmId).getLikes().contains(userId)) {
                filmStorage.removeLike(filmId, userId);
                log.info("Пользователь с ID {} удалил лайк у фильм с ID {}", userId, filmId);
            } else {
                throw new NotFoundException(String.format("Пользователь c ID %s не " +
                        "оценивал фильм с ID %s", userId, filmId));
            }
        } else {
            throw new NotFoundException(String.format("Нет фильма с ID %s", filmId));
        }
    }

    @Override
    public List<Film> returnPopularFilms(int count, int genreId, int year) {
        if (genreId == -1) {
            if (year == -1) {
                log.info("Был запрошен рейтинг популярных фильмов");
                return filmStorage.returnTopFilms(count);
            } else {
                log.info("Был запрошен рейтинг популярных фильмов по году {}", year);
                return filmStorage.returnTopFilmsByYear(count, year);
            }
        } else {
            if (year == -1) {
                log.info("Был запрошен рейтинг популярных фильмов по жанру {}", genreId);
                return filmStorage.returnTopFilmsByGenre(count, genreId);
            } else {
                log.info("Был запрошен рейтинг популярных фильмов по жанру {} и году {}", genreId, year);
                return filmStorage.returnTopFilmsByGenreAndYear(count, genreId, year);
            }
        }
    }

    @Override
    public List<Film> findSortFilmsByDirector(Integer directorId, String sortBy) {
        if (directorId == 0 || directorId == null) {
            throw new NotFoundException("wrong director id");
        }
        if (sortBy == null || sortBy.isBlank()) {
            throw new NotFoundException("sorting not specified");
        }
        return filmStorage.findSortFilmsByDirector(directorId, sortBy);
    }

    @Override
    public Set<Film> searchFilmsByDirectorOrName(String query, List<String> option) {
        log.info("Передан запрос на поиск фильма по названию/режиссеру");
        return filmStorage.searchFilmsByDirectorOrName(query, option);
    }

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
