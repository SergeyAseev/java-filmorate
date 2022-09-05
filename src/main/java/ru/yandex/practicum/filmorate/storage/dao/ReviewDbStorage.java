package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review findById(Long id) {
        final String sqlQuery = "SELECT R.ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL FROM REVIEWS R" +
                " JOIN USERS U ON U.ID = R.USER_ID" +
                " JOIN FILMS F ON F.ID = R.FILM_ID" +
                " WHERE R.ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeReview, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Отзыв по id=%s не найден!", id), e);
        }
    }

    @Override
    public List<Review> findAll() {
        final String sqlQuery = "SELECT R.ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL FROM REVIEWS R" +
                " JOIN USERS U ON U.ID = R.USER_ID" +
                " JOIN FILMS F ON F.ID = R.FILM_ID" +
                " GROUP BY R.ID " +
                " ORDER BY CASE WHEN USEFUL < 0 THEN 1 ELSE 0 END";
        return jdbcTemplate.query(sqlQuery, this::makeReview);
    }

    @Override
    public List<Review> findAllByFilmId(Long filmId, Integer count) {
        final String sqlQuery = "SELECT R.ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL FROM REVIEWS R" +
                " JOIN USERS U ON U.ID = R.USER_ID" +
                " JOIN FILMS F ON F.ID = R.FILM_ID" +
                " WHERE R.FILM_ID = ?" +
                " GROUP BY R.ID " +
                " ORDER BY CASE WHEN USEFUL < 0 THEN 1 ELSE 0 END" +
                " LIMIT ?";
        return filmId == null ? findAll() : jdbcTemplate.query(sqlQuery, this::makeReview, filmId, count);
    }

    @Override
    public Review save(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sqlQuery = "INSERT INTO REVIEWS(CONTENT, IS_POSITIVE, USER_ID, FILM_ID) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().longValue());
        review.setUseful(0L);
        return findById(review.getReviewId());
    }

    @Override
    public Review update(Review review) {
        final String sqlQuery = "UPDATE REVIEWS SET CONTENT = ?, IS_POSITIVE = ? WHERE ID = ?";
        jdbcTemplate.update(sqlQuery
                , review.getContent()
                , review.getIsPositive()
                , review.getReviewId()
        );
        return findById(review.getReviewId());
    }

    @Override
    public void deleteById(Long id) {
        final String sqlQuery = "DELETE FROM REVIEWS WHERE ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void setLike(Long id, Long userId, Boolean isPositive) {
        final String sqlQuery = "UPDATE REVIEWS SET IS_POSITIVE = ?, USER_ID = ?, USEFUL = ? WHERE ID = ?";
        final Review review = findById(id);
        jdbcTemplate.update(sqlQuery, isPositive, userId, review.getUseful() + 1, id);
    }

    @Override
    public void setDislike(Long id, Long userId, Boolean isPositive) {
        final String sqlQuery = "UPDATE REVIEWS SET IS_POSITIVE = ?, USER_ID = ?, USEFUL = ? WHERE ID = ?";
        final Review review = findById(id);
        jdbcTemplate.update(sqlQuery, isPositive, userId, review.getUseful() - 1, id);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        final String sqlQuery = "UPDATE REVIEWS SET IS_POSITIVE = ?, USER_ID = ?, USEFUL = ? WHERE ID = ?";
        final Review review = findById(id);
        jdbcTemplate.update(sqlQuery, null, userId, review.getUseful() - 1, id);
    }

    @Override
    public void deleteDislike(Long id, Long userId) {
        final String sqlQuery = "UPDATE REVIEWS SET IS_POSITIVE = ?, USER_ID = ?, USEFUL = ? WHERE ID = ?";
        final Review review = findById(id);
        jdbcTemplate.update(sqlQuery, null, userId, review.getUseful() + 1, id);
    }

    private Review makeReview(ResultSet resultSet, int rowNum) throws SQLException {
        return new Review(
                resultSet.getLong("ID"),
                resultSet.getString("CONTENT"),
                resultSet.getBoolean("IS_POSITIVE"),
                resultSet.getLong("USER_ID"),
                resultSet.getLong("FILM_ID"),
                resultSet.getLong("USEFUL")
        );
    }
}
