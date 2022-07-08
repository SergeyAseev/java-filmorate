package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InMemoryUserService implements UserService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public InMemoryUserService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage,
                               @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if(userStorage.retrieveUsers().containsKey(userId)){
            if (userStorage.retrieveUsers().containsKey(friendId)) {
                userStorage.retrieveUserById(userId).getFriends().add(friendId);
                userStorage.retrieveUserById(friendId).getFriends().add(userId);
                log.info("Пользователь {} и пользователь {} теперь друзья", userId, friendId);
            } else {
                throw new NotFoundException(String.format("Друг с ID %d не найден", friendId));
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", friendId));
        }
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        if(userStorage.retrieveUsers().containsKey(userId)){
            if (userStorage.retrieveUsers().containsKey(friendId)) {
                userStorage.retrieveUserById(userId).getFriends().remove(friendId);
                userStorage.retrieveUserById(friendId).getFriends().remove(userId);
                log.info("Пользователь {} и пользователь {} больше не друзья", userId, friendId);
            } else {
                throw new NotFoundException(String.format("Друг с ID %d не найден", friendId));
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", friendId));
        }
    }

    @Override
    public List<User> retrieveFriends(int userId) {
        List<User> friends = new ArrayList<>();
        if (userStorage.retrieveUsers().containsKey(userId)) {
            for (int user : userStorage.retrieveUserById(userId).getFriends()) {
                friends.add(userStorage.retrieveUserById(user));
            }
            return friends;
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", userId));
        }
    }

    @Override
    public List<User> retrieveCommonFriends(int userId, int friendId) {
        List<User> usersList = new ArrayList<>();
        List<User> friendsList = new ArrayList<>();
        if (userStorage.retrieveUsers().containsKey(userId)) {
            if (userStorage.retrieveUsers().containsKey(friendId)) {
                for (int user : userStorage.retrieveUserById(userId).getFriends()) {
                    usersList.add(userStorage.retrieveUserById(user));
                }
                for (int friend : userStorage.retrieveUserById(friendId).getFriends()) {
                    friendsList.add(userStorage.retrieveUserById(friend));
                }
                return usersList.stream().filter(friendsList::contains).collect(Collectors.toList());
            } else {
                throw new NotFoundException(String.format("Друг с ID %d не найден", friendId));
            }
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", userId));
        }
    }

    @Override
    public User addUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public void removeUserById(int userId) {
        userStorage.removeUserById(userId);
    }

    @Override
    public void removeAllUsers() {
        userStorage.removeAllUsers();
    }

    @Override
    public User retrieveUserById(int userId) {
        return userStorage.retrieveUserById(userId);
    }

    @Override
    public ArrayList<User> retrieveAllUsers() {
        return userStorage.retrieveAllUsers();
    }
}
