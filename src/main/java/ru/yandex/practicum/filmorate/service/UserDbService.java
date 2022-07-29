package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service("UserDbService")
public class UserDbService implements UserService{

    private final UserStorage userStorage;

    private final Map<Long, User> users = new LinkedHashMap<Long, User>();

    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(User user) {
        validate(user);
        userStorage.createUser(user);
        log.info("Добавлен пользователь id = {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        retrieveUserById(user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        userStorage.addFriends(userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userStorage.removeFromFriends(userId, friendId);
    }

    @Override
    public void removeUserById(long userId) {
        userStorage.removeUserById(userId);
    }

/*    @Override
    public void removeAllUsers() {

    }*/

    @Override
    public User retrieveUserById(long userId) {
        return userStorage.retrieveUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден пользователь с ID %s", userId)));
    }

    @Override
    public List<User> retrieveAllUsers() {
        return userStorage.retrieveAllUsers();
    }

    @Override
    public List<User> retrieveFriends(long userId) {
        return userStorage.getFriends(userId);
    }

    @Override
    public List<User> retrieveCommonFriends(long userId, long friendId) {
        return null;
    }

    protected void validate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения пользователя %s не может быть позже текущей даты");
        }
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email пользователя %s некорретный");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("login пользователя %s не может быть пустым");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
