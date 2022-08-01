package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final LikesDao likesDao;
    private final MpaRatingDao mpaRatingDao;
    private final GenreDao genreDao;

    @Override
    public Film addFilm(Film film) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("films")
                .usingColumns("name", "description", "release_date", "duration", "mpa_id")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of("name", film.getName(),
                        "description", film.getDescription(),
                        "release_date", Date.valueOf(film.getReleaseDate()),
                        "duration", film.getDuration(),
                        "mpa_id", film.getMpa().getId()
                ))
                .getKeys();
        film.setId((Long) keys.get("id"));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "MERGE INTO films (id, name, description, release_date, duration, mpa_id) KEY (id) VALUES (?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        return film;
    }

    @Override
    public void removeFilmById(long filmId) {
        String filmDeleteSql = "DELETE FROM films WHERE ID=?";
        jdbcTemplate.update(filmDeleteSql, ps -> {
            ps.setLong(1, filmId);
        });
    }

    @Override
    public Optional<Film> retrieveFilmById(long filmId) {
        String sql = "SELECT * FROM films WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.query(sql, this::makeFilm, filmId).stream().findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId))));
    }

    @Override
    public List<Film> retrieveAllFilms() {
        String sql = "SELECT * FROM films;";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "INSERT INTO LIKES values (?, ?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
        jdbcTemplate.update(sql, filmId, userId);
    }

    /**
     * Десериализация фильма
     * @param rs
     * @param rowNum
     * @return экземляр фильма
     */
    private Film makeFilm(ResultSet rs, int rowNum) {
        try {
            Film film = new Film(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    LocalDate.parse(rs.getString("release_date")),
                    rs.getInt("duration"));
            film.setMpa(mpaRatingDao.getFilmMpa(film.getId()));
            film.setGenres(genreDao.getFilmGenres(film.getId()));
            film.setLikes(likesDao.getFilmLikes(film.getId()));
            return film;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
