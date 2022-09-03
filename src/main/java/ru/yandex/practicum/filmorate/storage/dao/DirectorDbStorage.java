package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class DirectorDbStorage implements DirectorDao {

    private JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findAll() {
        String sqlQuery = "select * from DIRECTORS";
        List<Director> directors = jdbcTemplate.query(sqlQuery,this::makeDirector);
        return directors;
    }

    @Override
    public Director findDirectorById(Integer id) {
        String sqlQuery = "select * from DIRECTORS where DIRECTOR_ID = ?";
        final List<Director> director = jdbcTemplate.query(sqlQuery,this::makeDirector,id);
        if(director.isEmpty()) {
            throw new NotFoundException("нет режиссера с таким id");
        }
        return director.get(0);
    }

    @Override
    public Director createDirector(Director director) {
        String sqlQuery = "insert into DIRECTORS(DIRECTOR_NAME) values(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlQuery, new String[]{"DIRECTOR_ID"});
            ps.setString(1, director.getName());
            return ps;
        },keyHolder);
        director.setId(keyHolder.getKey().intValue());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        Director directorId = findDirectorById(director.getId());
        if (directorId != null) {
            String sqlQuery = "update DIRECTORS set DIRECTOR_NAME = ? where DIRECTOR_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    director.getName(),
                    director.getId());
            return director;
        }
        throw new NotFoundException("режисcёр с id не существует");
    }

    @Override
    public void removeDirector(Integer id) {
        String sqlQueryFilmDir = "delete from FILMS_DIRECTORS where DIRECTOR_ID = ?";
        jdbcTemplate.update(sqlQueryFilmDir,id);
        String sqlQuery = "delete from DIRECTORS where DIRECTOR_ID = ?";
        jdbcTemplate.update(sqlQuery,id);
    }

    @Override
    public Set<Director> findFilmDirectors(Long id) {
        final String sqlQuery = "select * from DIRECTORS d " +
                "left join FILMS_DIRECTORS fd on d.DIRECTOR_ID = fd. DIRECTOR_ID where FILM_ID = ? ";
        List<Director> directors = jdbcTemplate.query(sqlQuery,this::makeDirector,id);
        Set<Director> directorSet = new HashSet<>();
        directorSet.addAll(directors);
        return directorSet;
    }

    private Director makeDirector (ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getInt("DIRECTOR_ID"),
                rs.getString("DIRECTOR_NAME"));
    }
}
