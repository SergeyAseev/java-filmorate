package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {

    /**
     * Добавляем друга
     * @param userId кто добавляет
     * @param friendId кого добавляет
     */
    void addFriend(int userId, int friendId);

    /**
     * Удаляем друга
     * @param userId кто удаляет
     * @param friendId кого удаляет
     */
    void removeFriend(int userId, int friendId);

    /**
     * Возвращаем друзей
     * @param userId пользователь, у которого ищем друзей
     */
    List<User> retrieveFriends(int userId);

    /**
     * Возвращаем общих друзей
     * @param userId пользователь 1
     * @param friendId пользователь 2
     */
    List<User> retrieveCommonFriends(int userId, int friendId);

    User addUser(User user);

    User updateUser(User user);

    void removeUserById(int userId);

    void removeAllUsers();

    User retrieveUserById(int userId);

    ArrayList<User> retrieveAllUsers();
}
