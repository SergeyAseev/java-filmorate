package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.EventEnum;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationEnum;

import java.util.List;

public interface FeedDao {

    /**
     * Записываем событие в базу
     */
    void addFeed(long userId, EventEnum eventEnum, OperationEnum operationEnum, long entityId);

    /**
     * Получаем список событий конкретного пользователя
     * @param userId ID пользователя
     * @return список событий пользователя
     */
    List<Feed> getFeed(long userId);
}
