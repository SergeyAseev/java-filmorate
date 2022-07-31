package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class GenreDbStorage implements GenreDao{

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Genre> retrieveAllGenres() {
        String sql = "SELECT * FROM genre;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name"))
        );
    }

    @Override
    public Optional<Genre> retrieveGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE id=?;", id);
        if (genreRows.next()) {
            return Optional.of(new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            ));
        } else {
            throw new NotFoundException(String.format("Жанр с ID %d не найден", id));
        }
    }

    @Override
    public List<Genre> getFilmGenres(long id) {
        String sql = "SELECT fg.genre_id, g.name " +
                "FROM FILM_GENRE_LINKS fg " +
                "LEFT JOIN genre g ON fg.genre_id = g.id " +
                "WHERE fg.film_id=?;";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("name")),
                id);
        if (!genres.isEmpty()) {
            return new ArrayList<>(genres);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void fillingGenres(Film film) {
        String genreDeleteSql = "DELETE FROM film_genre_links WHERE film_id = ?";
        jdbcTemplate.update(genreDeleteSql, film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String sql = "MERGE INTO film_genre_links (film_id, genre_id) KEY (film_id, genre_id) values (?, ?);";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }

    }
}

