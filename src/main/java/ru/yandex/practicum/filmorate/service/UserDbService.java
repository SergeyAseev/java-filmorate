package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.EventEnum;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationEnum;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.FeedDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service("UserDbService")
public class UserDbService implements UserService{

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private final FeedDao feedDao;

    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage,
                         FeedDao feedDao) {
    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.feedDao = feedDao;
        this.filmStorage = filmStorage;
    }

    @Override
    public User addUser(User user) {
        validate(user);
        userStorage.createUser(user);
        log.info("Добавлен пользователь с ID = {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        retrieveUserById(user.getId());
        log.info("Обновлен пользователь с ID = {}", user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        retrieveUserById(friendId);
        userStorage.addFriends(userId, friendId);
        feedDao.addFeed(userId, EventEnum.FRIEND, OperationEnum.ADD, friendId);
        log.info("Добавлен друг с ID = {} пользователю с ID = {}", friendId, userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userStorage.removeFromFriends(userId, friendId);
        feedDao.addFeed(userId, EventEnum.FRIEND, OperationEnum.REMOVE, friendId);
        log.info("Удален друг с ID = {} у пользователя с ID = {}", friendId, userId);
    }

    @Override
    public void removeUserById(long userId) {
        userStorage.removeUserById(userId);
        log.info("Удален пользователь с ID = {}", userId);
    }


    @Override
    public User retrieveUserById(long userId) {
        return userStorage.retrieveUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден пользователь с ID %s", userId)));
    }

    @Override
    public List<User> retrieveAllUsers() {
        log.info("Возвращаем всех пользователей");
        return userStorage.retrieveAllUsers();
    }

    // Добавил проверки на наличие пользователей в двух методах ниже
    @Override
    public List<User> retrieveFriends(long userId) {
        if (userStorage.retrieveUserById(userId).isPresent()) {
            log.info("Возвращаем друзей пользователя с ID = {}", userId);
            return userStorage.getFriends(userId);
        } else {
            log.warn("Нет пользователя с ID = {}", userId);
            throw new NotFoundException("Не найден пользователь с ID " + userId);
        }
    }

    @Override
    public List<User> retrieveCommonFriends(long userId, long friendId) {
        if (userStorage.retrieveUserById(userId).isPresent()
                && userStorage.retrieveUserById(friendId).isPresent()) {
            log.info("Возвращаем общих друзей пользователей с ID = {} и с ID = {}", userId, friendId);
            return userStorage.getCommonFriends(userId, friendId);
        } else {
            log.warn("Нет пользователя с указанным ID");
            throw new NotFoundException("Не найден пользователь с указанным ID");
        }
    }

    @Override
    public List<Feed> retrieveUsersFeed(long userId) {
        retrieveUserById(userId);
        log.info("Возвращаем события для пользователя с ID = {}", userId);
        return feedDao.getFeed(userId);
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
