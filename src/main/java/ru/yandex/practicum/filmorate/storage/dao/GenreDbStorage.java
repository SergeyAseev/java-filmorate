package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

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
    public TreeSet<Genre> getFilmGenres(long id) {
        String sql = "SELECT fg.genre_id, g.name " +
                "FROM film_genre AS fg " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id=?;";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getInt("id"), rs.getString("name")), id);
        if (!genres.isEmpty()) {
            return new TreeSet<>(genres);
        } else {
            return null;
        }
    }
}
