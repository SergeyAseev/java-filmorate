package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("users")
                .usingColumns("login", "email", "name", "birthday")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of("login", user.getLogin(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "birthday", Date.valueOf(user.getBirthday())))
                .getKeys();
        user.setId((Long) keys.get("id"));
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users set id = ?, login = ?, email = ?, name = ?, birthday = ?;";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public List<User> retrieveAllUsers() {
        String sql = "SELECT * FROM users;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getString("name"),
                LocalDate.parse(rs.getString("birthday")))
        );
    }

    @Override
    public Optional<User> retrieveUserById(long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?;", userId);
        if (userRows.next()) {
            return Optional.of(new User(
                    userRows.getLong("id"),
                    userRows.getString("login"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    LocalDate.parse(userRows.getString("birthday"))));
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", userId));
        }
    }

    @Override
    public void addFriends(long userId, long friendId) {
        String sql = "INSERT INTO friends (from_user, to_user) VALUES (?, ?);";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        String sql = "SELECT fr.to_user, u.login, u.email, u.name, u.birthday " +
                "FROM users u " +
                "JOIN friends fr ON fr.to_user = u.id " +
                "WHERE fr.from_user = ?;";
        List<User> users = jdbcTemplate.query(sql, this::makeUser, userId);
        return List.copyOf(users);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sql = "SELECT fr.to_user, u.login, u.email, u.name, u.birthday, COUNT(to_user) " +
                "FROM friends fr " +
                "LEFT JOIN users u ON fr.to_user = u.id " +
                "WHERE from_user = ? OR from_user = ? " +
                "GROUP BY to_user " +
                "HAVING COUNT(to_user) > 1;";
        return jdbcTemplate.query(sql, this::makeUser, userId, friendId);

    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String sql = "DELETE FROM friends WHERE from_user = ? AND to_user = ?;";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeUserById(long userId) {
        String userDeleteSql = "DELETE FROM users WHERE ID=?";
        jdbcTemplate.update(userDeleteSql, ps -> {
            ps.setLong(1, userId);
        });
    }

    @Override
    public List<Long> getLikesByUser (long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT l.film_id from likes l" +
                " where l.user_id=?", id);
        List<Long> userList = new ArrayList<>();
        while(userRows.next())
        {
            userList.add(userRows.getLong("film_id"));
        }
        return userList;
    }

    private User makeUser(ResultSet rs, int rowNum) {
        try {
            return new User(
                    rs.getLong("to_user"),
                    rs.getString("login"),
                    rs.getString("email"),
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("birthday")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
