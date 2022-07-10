package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    /**
     * Добавляем друга
     * @param userId кто добавляет
     * @param friendId кого добавляет
     */
    void addFriend(long userId, long friendId);

    /**
     * Удаляем друга
     * @param userId кто удаляет
     * @param friendId кого удаляет
     */
    void removeFriend(long userId, long friendId);

    /**
     * Возвращаем друзей
     * @param userId пользователь, у которого ищем друзей
     */
    List<User> retrieveFriends(long userId);

    /**
     * Возвращаем общих друзей
     * @param userId пользователь 1
     * @param friendId пользователь 2
     */
    List<User> retrieveCommonFriends(long userId, long friendId);

    User addUser(User user);

    User updateUser(User user);

    void removeUserById(long userId);

    void removeAllUsers();

    User retrieveUserById(long userId);

    List<User> retrieveAllUsers();
}
