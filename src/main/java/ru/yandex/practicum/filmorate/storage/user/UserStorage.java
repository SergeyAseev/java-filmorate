package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Map;

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
    void removeUserById(int userId);

    /**
     * Удаляем всех пользователей
     */
    void removeAllUsers();

    /**
     * Получаем пользователя по ID
     * @return экземпляр пользователя
     */
    User retrieveUserById(int userId);

    /**
     * Получаем всех пользователей
     *
     * @return список всех значений LinkedHashMap, которая хранит всех пользователей
     */
    ArrayList<User> retrieveAllUsers();

    Map<Integer, User> retrieveUsers();
}
