package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.LikesLinks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikesLinksDbStorage implements LikesLinksDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<LikesLinks> getAllLikes() {
        String sql = "SELECT * FROM likes_links";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createLike(rs));
    }

    @Override
    public Set<Long> getFilmLikes(long filmId) {
        String sql = "SELECT USER_ID FROM likes_links WHERE FILM_ID = ?";
        return Set.copyOf(jdbcTemplate.query(sql,(rs,rowNum)->rs.getLong("USER_ID"),filmId));

    }

    private LikesLinks createLike(ResultSet rs) throws SQLException {
        int userId = rs.getInt("USER_ID");
        int filmId = rs.getInt("FILM_ID");
        return new LikesLinks(userId, filmId);
    }
}
