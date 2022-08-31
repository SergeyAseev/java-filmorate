package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

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

    /**
     * Создаем нового пользователя
     * @param user экземпляр текущего пользователя в виде Json
     * @return экземпляр созданного пользователя
     */
    User addUser(User user);

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
    User retrieveUserById(long userId);

    /**
     * Получаем всех пользователей
     * @return список значений, который хранит всех пользователей
     */
    List<User> retrieveAllUsers();

    /**
     * Возвращаем ленту событий пользователя
     * @param userId ID пользователя
     * @return список событий пользователя
     */
    List<Feed> retrieveUsersFeed(long userId);
    public List <Optional<Film>> getRecommendations(long id);
}
