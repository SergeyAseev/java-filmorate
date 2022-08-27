package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final LikesDao likesDao;
    private final MpaRatingDao mpaRatingDao;
    private final GenreDao genreDao;
    private final DirectorDao directorDao;

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
        if (film.getDirectors() != null) {
            addDirectorByFilm(film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "MERGE INTO FILMS (id, name, description, release_date, duration, mpa_id) KEY (id) VALUES (?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        if (film.getDirectors() != null && film.getDirectors().size() >= 0) {
            List<Director> directors = new LinkedList<>();
            if (film.getDirectors().size() > 0) {
                for (Director director : film.getDirectors()) {
                    if (!directors.contains(director)) {
                        directors.add(director);
                    }
                }
            }
            film.setDirectors(directors);
            deleteDirectorByFilm(film);
            addDirectorByFilm(film);
        } else {
            film.setDirectors(new ArrayList<>());
            deleteDirectorByFilm(film);
        }
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
        String sql = "SELECT * FROM films f left join MPA m ON f.MPA_ID = m.ID " +
                     "left join FILM_GENRE_LINKS fgl ON f.id = fgl.FILM_ID " +
                     "left join GENRE g on fgl.GENRE_ID = g.ID " +
                     "left join FILMS_DIRECTORS fd ON f.id = fd.FILM_ID " +
                     "left join DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID WHERE f.id = ?";
        return Optional.ofNullable(jdbcTemplate.query(sql, this::makeFilm, filmId).stream().findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId))));
    }

    @Override
    public List<Film> retrieveAllFilms() {
        String sql = "SELECT f.id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION FROM films f left join MPA m ON f.MPA_ID = m.ID " +
                     "left join FILM_GENRE_LINKS fgl ON f.id = fgl.FILM_ID " +
                     "left join GENRE g on fgl.GENRE_ID = g.ID " +
                     "left join FILMS_DIRECTORS fd ON f.id = fd.FILM_ID " +
                     "left join DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
                     "group by f.id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public List<Film> returnTopFilms(int count) {
        String sql = "SELECT f.id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, count(distinct l.USER_ID) FROM films f " +
                "left join MPA m ON f.MPA_ID = m.ID " +
                "left join FILM_GENRE_LINKS fgl ON f.id = fgl.FILM_ID " +
                "left join GENRE g on fgl.GENRE_ID = g.ID " +
                "left join likes l ON f.id = l.FILM_ID " +
                "group by f.id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION " +
                "order by count(distinct l.USER_ID) desc " +
                "limit ?; ";
        return jdbcTemplate.query(sql, this::makeFilm, count);
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

    @Override
    public List<Film> findSortFilmsByDirector(Integer directorId, String sortBy) {
        if (findIdDirector(directorId) == null || findIdDirector(directorId).isEmpty()) {
            throw new NotFoundException("Режиссера не существует");
        }
        if (sortBy.equals("year")) {
                String sqlQuery = "SELECT * FROM films f left join MPA m ON f.MPA_ID = m.ID " +
                        "left join FILM_GENRE_LINKS fgl ON f.id = fgl.FILM_ID " +
                        "left join GENRE g on fgl.GENRE_ID = g.ID " +
                        "left join FILMS_DIRECTORS fd ON f.id = fd.FILM_ID " +
                        "left join DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID WHERE fd.DIRECTOR_ID = ? " +
                        "order by extract(year from f.RELEASE_DATE) ";
                List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, directorId);
                return films;
        } else if (sortBy.equals("likes")) {
                String sqlQuery = "SELECT f.id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, count(distinct l.USER_ID) " +
                        "FROM films f left join MPA m ON f.MPA_ID = m.ID " +
                        "left join FILM_GENRE_LINKS fgl ON f.id = fgl.FILM_ID " +
                        "left join GENRE g on fgl.GENRE_ID = g.ID " +
                        "left join likes l ON f.id = l.FILM_ID " +
                        "left join FILMS_DIRECTORS fd ON f.id = fd.FILM_ID " +
                        "left join DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID WHERE fd.DIRECTOR_ID = ? " +
                        "group by f.id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION " +
                        "order by count(l.USER_ID) desc";
                List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, directorId);
                return films;
        }
        return null;
    }

    public void addDirectorByFilm(Film film) {
        final String sqlQuery ="insert into FILMS_DIRECTORS(FILM_ID,DIRECTOR_ID) VALUES(?,?)";
        for (Director director : film.getDirectors()) {
            jdbcTemplate.update(sqlQuery, film.getId(), director.getId());
        }
    }

    public List<Integer> findIdDirector(Integer directorId) {
        final String sqlQuery = "select FILM_ID from FILMS_DIRECTORS where DIRECTOR_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class,directorId);
    }
    public void deleteDirectorByFilm(Film film) {
        final String sqlQuery = "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
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
                film.setDirectors(directorDao.getDirectorByFilmID(film.getId()));
                film.setLikes(likesDao.getFilmLikes(film.getId()));
                return film;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }
}
