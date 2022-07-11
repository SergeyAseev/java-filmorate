package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
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
     * Удаляем всех пользователей
     */
    void removeAllUsers();

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

    Map<Long, User> retrieveUsers();
}
