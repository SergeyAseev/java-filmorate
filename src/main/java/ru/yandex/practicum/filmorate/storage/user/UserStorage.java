package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    /**
     * Создаем нового пользователя
     * @param user экземпляр текущего пользователя в виде Json
     * @return экземпляр созданного пользователя
     */
    User createUser(User user);

    /**
     * Обновляем данные уже существующего пользователя
     * @param user экземпляр текущего пользователя
     * @return экземпляр обновленного пользователя
     */
    User updateUser(User user);

    /**
     * Удаляем пользователя по ID
     */
    void removeUserById(long userId);

    /**
     * Получаем пользователя по ID
     * @return экземпляр пользователя
     */
    Optional<User> retrieveUserById(long userId);

    /**
     * Получаем всех пользователей
     *
     * @return список всех значений LinkedHashMap, которая хранит всех пользователей
     */
    List<User> retrieveAllUsers();

    void addFriends(long userId, long friendId);

    List<User> getFriends(long userId);

    List<User> getCommonFriends(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);

    List<Long> getLikesByUser (long id);
}
