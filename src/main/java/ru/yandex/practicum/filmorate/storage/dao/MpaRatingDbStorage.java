package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Component
public class MpaRatingDbStorage implements MpaRatingDao{

    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Mpa> retrieveAllMpaRatings() {
        String sql = "SELECT * FROM mpa;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name"))
        );
    }

    @Override
    public Optional<Mpa> retrieveMpaRatingById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id=?;", id);
        if (genreRows.next()) {
            return Optional.of(new Mpa(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            ));
        } else {
            throw new NotFoundException(String.format("Рейтинг с ID %d не найден", id));
        }
    }

    @Override
    public Mpa getFilmMpa (long id){
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(
                "SELECT f.mpa_id, mr.name " +
                        "FROM films f " +
                        "LEFT JOIN mpa mr ON f.mpa_id = mr.id " +
                        "WHERE f.id=?;", id);
        if (mpaRows.next()) {
            Mpa mpaRating = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("name"));
            return mpaRating;
        } else {
            return null;
        }
    }
}

