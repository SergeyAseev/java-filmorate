package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Likes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikesDbStorage implements LikesDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Likes> getAllLikes() {
        String sql = "SELECT * FROM likes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createLike(rs));
    }

    @Override
    public Set<Long> getFilmLikes(long filmId) {
        String sql = "SELECT USER_ID FROM likes WHERE FILM_ID = ?";
        return Set.copyOf(jdbcTemplate.query(sql,(rs,rowNum)->rs.getLong("USER_ID"),filmId));

    }

    private Likes createLike(ResultSet rs) throws SQLException {
        int userId = rs.getInt("USER_ID");
        int filmId = rs.getInt("FILM_ID");
        return new Likes(userId, filmId);
    }
}
