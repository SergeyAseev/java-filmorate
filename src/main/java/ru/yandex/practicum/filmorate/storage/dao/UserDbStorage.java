package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<User> retrieveUserById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?;", id);
        if (userRows.next()) {
            return Optional.of(new User(
                    userRows.getLong("id"),
                    userRows.getString("login"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    LocalDate.parse(userRows.getString("birthday"))));
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", id));
        }
    }

    @Override
    public Map<Long, User> retrieveUsers() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
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
        return null;
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String sql = "DELETE FROM friends WHERE from_user = ? AND to_user = ?;";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeUserById(long id) {
        String userDeleteSql = "DELETE FROM USERS WHERE ID=?";
        jdbcTemplate.update(userDeleteSql, ps -> {
            ps.setLong(1, id);
        });
    }



/*
    @Override
    public Friendship getFriendship(int userId, int friendId) {
        SqlRowSet friendRows = queryFriendship(userId, friendId);
        if (friendRows.next()) {
            return new Friendship(
                    friendRows.getInt("from_user_id"),
                    friendRows.getInt("to_user_id"),
                    friendRows.getBoolean("is_bilateral"));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("У пользователя с id %d нет в друзьях пользователя с id %d", userId, friendId));
        }
    }*/

/*    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String sql = "SELECT fr.to_user_id, u.login, u.email, u.name, u.birthday, COUNT (to_user_id) " +
                "FROM friendship AS fr " +
                "LEFT OUTER JOIN users AS u ON fr.to_user_id = u.id " +
                "WHERE from_user_id = ? OR from_user_id = ? " +
                "GROUP BY to_user_id " +
                "HAVING COUNT (to_user_id) > 1;";
        Collection<User> users = jdbcTemplate.query(sql, this::makeUser, userId, friendId);
        return List.copyOf(users);
    }*/

/*    private SqlRowSet queryFriendship(int userId, int friendId) {
        return jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM friends " +
                        "WHERE from_user_id = ? AND to_user_id = ?;",
                userId, friendId);
    }*/

    private User makeUser(ResultSet rs, int rowNum) {
        try {
            return new User(
                    rs.getLong("id"),
                    rs.getString("login"),
                    rs.getString("email"),
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("birthday")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}