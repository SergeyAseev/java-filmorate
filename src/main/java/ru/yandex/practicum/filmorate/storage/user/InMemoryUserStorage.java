package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    protected long userId = 0;
    private final Map<Long, User> users = new LinkedHashMap<Long, User>();

    /**
     * увеличивает уникальный идентификатор пользователя
     */
    protected long generateId() {
        return ++userId;
    }

    @Override
    public User createUser(User user) {
        validate(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь c ID {} успешно добавлен", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        users.put(user.getId(), user);
        log.info("Пользователь c ID {} успешно обновлен", user.getId());
        return user;
    }

    @Override
    public void removeUserById(long userId) {
        if (!users.containsKey(userId)) {
            return;
        }
        users.remove(userId);
        log.info("Пользователь c ID {} успешно удален", userId);
    }

/*    @Override
    public void removeAllUsers() {
        users.clear();
        log.info("Все пользователи удалены");
    }*/

    @Override
    public Optional<User> retrieveUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> retrieveAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Map<Long, User> retrieveUsers() {
        return users;
    }

    @Override
    public void addFriends(long userId, long friendId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public List<User> getFriends(long userId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Проверка создаваемого пользователя на валидность
     * @param user экземпляр текущего пользователя
     */
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
