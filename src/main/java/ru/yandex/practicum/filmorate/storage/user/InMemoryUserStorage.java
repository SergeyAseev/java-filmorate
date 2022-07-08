package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    protected int userId = 0;
    private final Map<Integer, User> users = new LinkedHashMap<Integer, User>();

    /**
     * увеличивает уникальный идентификатор пользователя
     */
    protected int generateId() {
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
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format(
                    "Пользователь %s не найден", user.getId()));
        }
        validate(user);
        users.put(user.getId(), user);
        log.info("Пользователь c ID {} успешно обновлен", user.getId());
        return user;
    }

    @Override
    public void removeUserById(int userId) {
        if (!users.containsKey(userId)) {
            return;
        }
        users.remove(userId);
        log.info("Пользователь c ID {} успешно удален", userId);
    }

    @Override
    public void removeAllUsers() {
        users.clear();
        log.info("Все пользователи удалены");
    }

    @Override
    public User retrieveUserById(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format(
                    "Нельзя удалить пользователя с ID %s", userId));
        }

        return users.get(userId);
    }

    @Override
    public ArrayList<User> retrieveAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Map<Integer, User> retrieveUsers() {
        return users;
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
